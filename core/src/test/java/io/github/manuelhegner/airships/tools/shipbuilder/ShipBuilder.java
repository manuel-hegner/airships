package io.github.manuelhegner.airships.tools.shipbuilder;

import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.util.Point;

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
			Point p = findStart(img);
			List<Point> points = new ArrayList<>();
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
			
			boolean reduced = true;
			while(reduced == true) {
				reduced = false;
				
				Pair<Integer, Double> minPoint = IntStream
					.range(2, points.size())
					.mapToObj(i -> Pair.of(i, area(points.get(i-2),points.get(i-1),points.get(i))))
					.min(Comparator.comparing(Pair::getRight))
					.get();
				
				if(minPoint.getRight() < 3d) {
					reduced = true;
					points.remove(minPoint.getLeft()-1);
				}
			}
			points.remove(points.size()-1);
			
			System.out.println("\t\t"+points.size()+" points after reduction");
			result
				.body(template.getBody());
				
			target.getParentFile().mkdirs();
			SHIPTYPE_WRITER.writeValue(target, result.build());
			File imgTarget = target.toPath().getParent().resolve(template.getImage().toPath()).toFile();
			imgTarget.getParentFile().mkdirs();
			ImageIO.write(img, "png", imgTarget);
			
			if(debug != null) {
				debug.getParentFile().mkdirs();
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ImageIO.write(img, "png", baos);
				String img64 = Base64.getEncoder().encodeToString(baos.toByteArray());
				String svg =  "<svg width=\""+img.getWidth()+"\" height=\""+img.getHeight()+"\">"
					+"<image width=\""+img.getWidth()+"\" height=\""+img.getHeight()+"\" xlink:href=\"data:image/png;base64,"+img64+"\"/>"
					+"<polyline points=\""+points.stream().map(c->c.getX()+","+c.getY()).collect(Collectors.joining(" "))+"\" style=\"fill:none;stroke:green;stroke-width:0.2\"/>"
					+"</svg>";
				try(BufferedWriter w = Files.newBufferedWriter(debug.toPath())) {
					w.write(svg);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static double area(Point a, Point b, Point c) {
		return 1d/2d * Math.abs(a.getX()*(b.getY()-c.getY()) + b.getX()*(c.getY()-a.getY()) + c.getX()*(a.getY()-b.getY()));
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
		if(x<0 || y<0 || x>img.getWidth() || y>img.getHeight())
			return true;
		/*
		Color color = new Color(img.getRGB(x, y), true);
		return color.getAlpha()==0;
		*/
		return  ((img.getRGB(x, y) >> 24) & 0xff) == 0;
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
