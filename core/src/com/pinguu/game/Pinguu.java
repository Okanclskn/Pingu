package com.pinguu.game;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import java.util.Random;

public class Pinguu extends Game {
	private SpriteBatch batch;
	private OrthographicCamera kamera,kamera2;
	private Texture ArkaPlan,Pingu;
	private Vector2 pinguPozisyon;
	private Random rand;
	private TextureRegion zemin,bsmk,oyunbitti;
	private static float baslangicX=50;
	private static float baslangicY=30;
	private float gecenZaman=0;
	private float ilkzeminPosX;
	private Array<Basamak> basamaklar = new Array<Basamak>();
	private Rectangle pingucrcv = new Rectangle();
	private Rectangle kutucrcv = new Rectangle();
	private int puan=0;
	private BitmapFont font;
	private int maxzplm=50;


	private enum OyunDurumu{Bslngc,Clsyr,OynBtt}
	private OyunDurumu oyunDurumu = OyunDurumu.Bslngc;
	private Vector2 yercekimi = new Vector2();
	private Vector2 pinguYercekimi = new Vector2();
	private static final float zplm=380;
	private static final float yrckm= -10;
	private static final float pinguHIz=210;




	@Override
	public void create () {
		batch = new SpriteBatch();
		kamera = new OrthographicCamera();
		kamera2 = new OrthographicCamera();
		pinguPozisyon = new Vector2();
		rand = new Random();
		kamera.setToOrtho(false,800,480);
		kamera2.setToOrtho(false,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		kamera2.update();
		ArkaPlan = new Texture("arkaplan.png");
		Pingu = new Texture("pingu.png");
		zemin = new TextureRegion(new Texture("Zemin.png"));
		bsmk = new TextureRegion(new Texture("kutu.png"));
		oyunbitti = new TextureRegion(new Texture("oyunbitti.png"));
		font = new BitmapFont(Gdx.files.internal("arial.fnt"));
		Baslangic();
	}

	private void Baslangic() {
		pinguPozisyon.set(baslangicX,baslangicY);
		kamera.position.x = 400;
		yercekimi.set(0,yrckm);
		pinguYercekimi.set(0,0);
		ilkzeminPosX = 0;
		basamaklar.clear();
		int c=300;
		for(int i=0; i<5; i++) {
			int temp=rand.nextInt(1000)+900+c;
			basamaklar.add(new Basamak(temp, 20, bsmk));
			 c=temp;
		}
		puan=0;
	}

	@Override
	public void render () {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		kamera.update();
		batch.setProjectionMatrix(kamera.combined);
		batch.begin();
		batch.draw(ArkaPlan, kamera.position.x - ArkaPlan.getWidth()/2, 0);
		for(Basamak basamak : basamaklar){
			batch.draw(basamak.bsmkrsm,basamak.pozisyon.x,basamak.pozisyon.y);
		}
		batch.draw(zemin,ilkzeminPosX,0);
		batch.draw(zemin,ilkzeminPosX+zemin.getRegionWidth(),0);
		batch.draw(Pingu,pinguPozisyon.x,pinguPozisyon.y);
		batch.end();

		batch.setProjectionMatrix(kamera2.combined);
		batch.begin();
		if(oyunDurumu == OyunDurumu.OynBtt){
			batch.draw(oyunbitti,Gdx.graphics.getWidth()/2 - oyunbitti.getRegionWidth()/2,Gdx.graphics.getHeight()/2 - oyunbitti.getRegionHeight()/2);
		}
		if(oyunDurumu == OyunDurumu.OynBtt || oyunDurumu == OyunDurumu.Clsyr){
			font.draw(batch,""+puan,Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()-60);
		}
		batch.end();
		update();
	}


	private void update(){
		float deltaTime = Gdx.graphics.getDeltaTime();
		gecenZaman+=deltaTime;

		if(Gdx.input.justTouched()) {
			if (oyunDurumu == OyunDurumu.Bslngc) {
				oyunDurumu = OyunDurumu.Clsyr;
			}

			if (oyunDurumu == OyunDurumu.Clsyr) {
				if(maxzplm > pinguPozisyon.y)
					pinguYercekimi.set(pinguHIz, zplm);
			}

			if (oyunDurumu == OyunDurumu.OynBtt) {
				oyunDurumu = OyunDurumu.Bslngc;
				Baslangic();
			}
		}
		if (oyunDurumu != OyunDurumu.Bslngc) {
			pinguYercekimi.add(yercekimi);
		}

		pinguPozisyon.mulAdd(pinguYercekimi,deltaTime);
		kamera.position.x = pinguPozisyon.x+350;

		if(kamera.position.x > zemin.getRegionWidth() + ilkzeminPosX + 400){
			ilkzeminPosX+=zemin.getRegionWidth();
		}
		if(pinguPozisyon.y <31){
			pinguPozisyon.y=30;
		}
		pingucrcv.set(pinguPozisyon.x,pinguPozisyon.y,Pingu.getWidth(),Pingu.getHeight());

		for(Basamak basamak : basamaklar){
			kutucrcv.set(basamak.pozisyon.x,basamak.pozisyon.y,(basamak.bsmkrsm.getRegionWidth()-30/2+20),basamak.bsmkrsm.getRegionHeight()-5);
			if(kamera.position.x-basamak.pozisyon.x > 400 + basamak.bsmkrsm.getRegionWidth()){
				basamak.pozisyon.x+=5*200;
				basamak.pozisyon.y=20;
				basamak.bsmkrsm=this.bsmk;
				System.out.println(basamak.pozisyon.x);
			}
			if(pingucrcv.overlaps(kutucrcv)){
				oyunDurumu=OyunDurumu.OynBtt;
				pinguYercekimi.x=0;
			}
		}
		if(oyunDurumu == OyunDurumu.Clsyr)
			puan++;
	}

	@Override
	public void dispose () {
		batch.dispose();
		ArkaPlan.dispose();
	}
}
