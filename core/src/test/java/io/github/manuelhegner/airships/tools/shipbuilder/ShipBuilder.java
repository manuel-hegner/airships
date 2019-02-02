package io.github.manuelhegner.airships.tools.shipbuilder;

import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.util.Point;

import com.badlogic.gdx.math.Vector2;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;

import io.github.manuelhegner.airships.entities.ShipType;
import io.github.manuelhegner.airships.entities.ShipType.ShipTypeBuilder;
import io.github.manuelhegner.airships.util.io.Jackson;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class ShipBuilder {

	private static final ObjectReader SHIPTYPE_READER = Jackson.MAPPER.readerFor(ShipTypeTemplate.class);
	private static final ObjectWriter SHIPTYPE_WRITER = Jackson.MAPPER.writerFor(ShipType.class).withDefaultPrettyPrinter();
	
	public static void main(String[] args) throws IOException {
		if(args.length != 2 && args.length != 3) {
			System.out.println("Requires two or three arguments");
			return;
		}
		
		File input = new File(args[0]);
		File output = new File(args[1]);
		File debug = args.length == 3 ? new File(args[2]) : null;
		Files
			.walk(input.toPath())
			.parallel()
			.filter(p->p.toString().endsWith(".shiptype.json"))
			.map(Path::toFile)
			.filter(File::isFile)
			.forEach(f -> transform(
				f,
				output.toPath().resolve(input.toPath().relativize(f.toPath())).toFile(),
				debug!=null ? debug.toPath().resolve(input.toPath().relativize(new File(f.getParentFile(), f.getName()+".svg").toPath())).toFile() : null
			));
	}

	private static void transform(File source, File target, File debug) {
		System.out.println("\tanalyzing "+source);
		try {
			ShipTypeBuilder<?,?> result = ShipType.builder();
			ShipTypeTemplate template = SHIPTYPE_READER.readValue(source);
			
			BufferedImage img = ImageIO.read(source.toPath().getParent().resolve(template.getImage().toPath()).toFile());
			result
				.name(template.getName())
				.length(template.getLength().orElse(img.getHeight()))
				.width(img.getWidth())
				.height(img.getHeight())
				.centerX(template.getCenterX().orElse(img.getWidth()/2f))
				.centerY(template.getCenterY().orElse(img.getWidth()/2f))
				.image(template.getImage())
				;
			
			
			//find start pixel
			
			List<Point> points = new ArrayList<>();
			Point p = findStart(img);
			Direction last = Direction.RIGHT;
			do {
				points.add(p);
				for(Direction d:last.getNext()) {
					Point next = d.step(p);
					if(!isTransparent(img, next.getX(), next.getY())) {
						p = next;
						last = d;
						break;
					}
				}
			} while(!p.equals(points.get(0)));
			points.add(p);
			
			System.out.println("\t\t"+points.size()+" points");
			
			double normalizedReductionThreshold = (img.getWidth()*img.getHeight())/2000d;
			
			boolean reduced = true;
			while(reduced == true) {
				reduced = false;
				
				Pair<Integer, Double> minPoint = IntStream
					.range(2, points.size())
					.mapToObj(i -> Pair.of(i, area(points.get(i-2),points.get(i-1),points.get(i))))
					.min(Comparator.comparing(Pair::getRight))
					.get();
				
				if(minPoint.getRight() < normalizedReductionThreshold) {
					reduced = true;
					points.remove(minPoint.getLeft()-1);
				}
			}
			points.remove(points.size()-1);
			
			System.out.println("\t\t"+points.size()+" points after reduction");
			result
				.body(template.getBody());
			
			//triangulate the polygon
			List<Integer> triangles = Earcut
				.earcut(points.stream().flatMapToDouble(z->DoubleStream.of(z.getX(), z.getY())).toArray());

			for(int i=0;i<triangles.size();i+=3) {
				int[] tri = new int[] {
					triangles.get(i),
					triangles.get(i+1),
					triangles.get(i+2)
				};
				Arrays.sort(tri);
				//ArrayUtils.reverse(tri);
				
				result.shape(Arrays.stream(tri).mapToObj(points::get).toArray(Point[]::new));
			}
			System.out.println("\t\t"+(triangles.size()/3)+" triangles");
			
			ShipType fin = result.build();
			
			fin.calculateDragCoefficients();
			
			target.getParentFile().mkdirs();
			SHIPTYPE_WRITER.writeValue(target, fin);
			File imgTarget = target.toPath().getParent().resolve(template.getImage().toPath()).toFile();
			imgTarget.getParentFile().mkdirs();
			ImageIO.write(img, "png", imgTarget);
			
			if(debug != null) {
				debug.getParentFile().mkdirs();
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ImageIO.write(img, "png", baos);
				String img64 = Base64.getEncoder().encodeToString(baos.toByteArray());
				String svg =  "<svg width=\""+img.getWidth()+"\" height=\""+img.getHeight()+"\">"
					+"<image width=\""+img.getWidth()+"\" height=\""+img.getHeight()+"\" xlink:href=\"data:image/png;base64,"+img64+"\"/>\n";
				for(Point[] sh : fin.getShapes())
					svg+="<polygon points=\""+Arrays.stream(sh).map(c->c.getX()+","+c.getY()).collect(Collectors.joining(" "))+"\" style=\"fill:none;stroke:green;stroke-width:0.2\"/>\n";
				svg+="</svg>";
				try(BufferedWriter w = Files.newBufferedWriter(debug.toPath())) {
					w.write(svg);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static double area(Point a, Point b, Point c) {
		double area = 1d/2d * Math.abs(
			  a.getX()*(b.getY()-c.getY()) 
			+ b.getX()*(c.getY()-a.getY()) 
			+ c.getX()*(a.getY()-b.getY())
		);
		
		//punish negative spaces
		Vector2 v1 = new Vector2(b.getX()-a.getX(), b.getY()-a.getY());
		Vector2 v2 = new Vector2(b.getX()-c.getX(), b.getY()-c.getY());
		if((360+v1.angle(v2))%360 < 180)
			area *= 3;
		
		return area;
	}

	private static Point findStart(BufferedImage img) {
		for(int y = 0; y < img.getHeight(); y++) {
			for(int x = 0; x < img.getWidth(); x++) {
				if(!isTransparent(img, x, y)) {
					Point p = new Point(x, y);
					return p;
				}
			}
		}
		throw new IllegalStateException("The image seems empty");
	}

	private static boolean isTransparent(BufferedImage img, int x, int y) {
		if(x<0 || y<0 || x>=img.getWidth() || y>=img.getHeight())
			return true;
		/*
		Color color = new Color(img.getRGB(x, y), true);
		return color.getAlpha()==0;
		*/
		return  ((img.getRGB(x, y) >> 24) & 0xff) < 120;
	}
	
	@RequiredArgsConstructor
	public static enum Direction {
		LEFT(-1,0),
		TOP_LEFT(-1,-1),
		TOP(0,-1),
		TOP_RIGHT(1,-1),
		RIGHT(1,0),
		BOTTOM_RIGHT(1,1),
		BOTTOM(0,1),
		BOTTOM_LEFT(-1,1);
		
		private final int dx;
		private final int dy;
		@Getter(lazy=true)
		private final Direction[] next = calculateNext();
		
		public Point step(Point start) {
			return new Point(start.getX()+dx, start.getY()+dy);
		}
		
		private Direction[] calculateNext() {
			Direction[] values = Direction.values();
			Direction[] result = new Direction[values.length];
			int next = (this.ordinal()+5)%8;
			System.arraycopy(values, next, result, 0, values.length - next);
			System.arraycopy(values, 0, result, values.length - next, next);
			
			return result;
		}
	}
}
