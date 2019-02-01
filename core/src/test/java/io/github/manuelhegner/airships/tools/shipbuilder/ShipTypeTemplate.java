package io.github.manuelhegner.airships.tools.shipbuilder;

import java.io.File;
import java.util.Optional;

import com.badlogic.gdx.physics.box2d.BodyDef;

import lombok.Data;

@Data
public class ShipTypeTemplate {
	private File image;
	private Optional<Integer> length;
	private String name;
	private Optional<Float> centerX;
	private Optional<Float> centerY;
	private BodyDef body = new BodyDef();
}
