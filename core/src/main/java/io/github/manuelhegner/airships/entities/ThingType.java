package io.github.manuelhegner.airships.entities;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.utils.Disposable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter @SuperBuilder @NoArgsConstructor
public abstract class ThingType<SELF extends ThingType<SELF, THING>, THING extends Thing<THING, SELF>> implements Disposable {

	@Override
	public void dispose() {}

	protected abstract BodyDef getBodyDef();
}
