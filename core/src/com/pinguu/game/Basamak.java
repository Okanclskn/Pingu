package com.pinguu.game;

        import com.badlogic.gdx.graphics.g2d.TextureRegion;
        import com.badlogic.gdx.math.Vector2;




public class Basamak {
    Vector2 pozisyon = new Vector2();
    TextureRegion bsmkrsm;

    public Basamak(float x, float y, TextureRegion bsmkrsm) {
        this.pozisyon.x=x;
        this.pozisyon.y=y;
        this.bsmkrsm = bsmkrsm;
    }
}
