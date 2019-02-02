package io.github.manuelhegner.airships.tools.shipbuilder;

import java.io.File;
import java.util.Optional;

import com.badlogic.gdx.physics.box2d.BodyDef;

import lombok.Data;

@Data
public class ShipTypeTemplate {
	private File image;
	private Optional<Integer> length = Optional.empty();
	private String name;
	private Optional<Float> centerX = Optional.empty();
	private Optional<Float> centerY = Optional.empty();
	private BodyDef body = new BodyDef();
}
