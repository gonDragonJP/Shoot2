package com.gondragon.shoot2.enemy;

import com.gondragon.shoot2.Global;
import com.gondragon.shoot2.myplane.CallbackOfMyPlane;
import com.gondragon.shoot2.myplane.MyPlane;
import com.gondragon.shoot2.animation.AnimationData;
import com.gondragon.shoot2.animation.AnimationManager;
import com.gondragon.shoot2.animation.AnimationSet;
import com.gondragon.shoot2.vector.Double2Vector;
import com.gondragon.shoot2.vector.Int2Vector;

import java.util.ArrayList;

public class Enemy extends ProtoEnemy{

    //private ItemGenerator itemGenerator;
    protected CallbackOfMyPlane cbOfMyPlane;
    protected CallbackOfGeneratingChild cbOfGeneratingChild;

    protected CalcParam calcParam;

    public float fx, fy;
    public Double2Vector velocity;
    public Double2Vector acceleration;
    public boolean isHoming;
    public Double2Vector homingAcceleration;
    public ArrayList<CollisionRegion> collisionRotated;

    public int nodeFrameCount;
    public int nodeDurationFrame;
    public int nodeIndex;

    public int genFrameCount;
    public int genIndex;
    public int genStartFrame, genIntervalFrame, genRepeat;
    public int genObjectID;
    public Enemy tempGenChild;

    protected int animeFrame, totalAnimeFrame;
    public AnimationSet animeSet;
    public AnimationSet.AnimeKind animeKind;

    public double drawAngle, oldDrawAngle;

    private CollisionChecker collisionChecker;

    public Enemy(
            CallbackOfMyPlane cbOfMyPlanePos,
            CallbackOfGeneratingChild cbOfGeneratingChild
    ){
        super();
        //itemGenerator = container.itemGenerator;

        this.cbOfMyPlane = cbOfMyPlanePos;
        this.cbOfGeneratingChild = cbOfGeneratingChild;

        initialize();
    }

    private void initialize(){

        velocity = new Double2Vector();
        acceleration = new Double2Vector();
        homingAcceleration = new Double2Vector();
        collisionRotated = new ArrayList<CollisionRegion>();
        calcParam = new CalcParam();

        isHoming = false;
        animeKind = AnimationSet.AnimeKind.NORMAL;
    }

    public void destroy(){

        if(collisionChecker !=null) collisionChecker.setActive(false);
    }

    public void setData(

            EnemyData enemyData,
            Int2Vector requestPos,
            Enemy parentEnemy
    ){

        super.setData(enemyData, requestPos, parentEnemy);

        setStartPosition(requestPos);
        setMovingNode(0);  //初速、加速度などがセットされます

        if(myData.generator.size()>0)	setGenerating(0);

        for(CollisionRegion e: myData.collision){

            CollisionRegion col = new CollisionRegion(e);
            this.collisionRotated.add(col);
        }

        if(myData.collision.size()!=0) {
            collisionChecker = new CollisionChecker(this);
            collisionChecker.setActive(true);
        }

        animeSet = myData.animationSet;
        drawAngle = AnimationManager.getEnemyRotateAngle(animeSet.normalAnime, this, true);
        //tendAheadの為にnodesetの後でないとマズイ(子がすぐに参照する可能性あり）
    }

    private void setStartPosition(Int2Vector requestPos){

        Int2Vector startPos = myData.startPosition;
        Int2Vector startAttrib = myData.startPosAttrib;

        if(parentEnemy == null){

            startPos = EnemyCalculator.getStartPositionWithAtrib
                    (cbOfMyPlane.getMyPlanePos(), startPos, startAttrib);
        }
        else{
            startPos = parentEnemy.getChildStartPositionFromRequest(requestPos);
        }

        fx = this.x = startPos.x;		fy = this.y = startPos.y;
    }

    public Int2Vector getChildStartPositionFromRequest(Int2Vector requestPos){

        double c = Math.cos(drawAngle * Global.radian);
        double s = Math.sin(drawAngle * Global.radian);

        int childX = x + (int)(requestPos.x * c - requestPos.y * s);
        int childY = y + (int)(requestPos.x * s + requestPos.y * c);

        return new Int2Vector(childX, childY);
    }

    public double getAngleOfTendToPlane(){

        Int2Vector myPlane = cbOfMyPlane.getMyPlanePos();

        return -90 +
                Math.atan2(myPlane.y - y, myPlane.x - x) / Global.radian;
    }

    public double getAngleOfTendAhead(){

        return -90 +
                Math.atan2(velocity.y, velocity.x) / Global.radian;
    }

    public Double2Vector getUnitVectorOfFaceOn(){

        Double2Vector result = new Double2Vector();

        double angle = (drawAngle + 90) * Global.radian;
        result.set(Math.cos(angle), Math.sin(angle));

        return result;
    }

    public Double2Vector getDrawSizeOfNormalAnime(){

        return animeSet.normalAnime.drawSize;
    }

    public void setExplosion(){

        isInExplosion = true;
        animeKind = AnimationSet.AnimeKind.EXPLOSION;
        totalAnimeFrame = 0;

        hasShadow = false;
        //SoundEffect.play(SoundKind.EXPLOSION1);
    }

	/*public void dropItem(boolean isShootByLaser){

	if(isShootByLaser)

		itemGenerator.addWeaponEnergy(x, y);
	else
		itemGenerator.addShieldEnergy(x, y);
	}*/

    protected void setMovingNode(int index){

        MovingNode nd = myData.node.get(index);

        Int2Vector myPlane = cbOfMyPlane.getMyPlanePos();

        calcParam.initialize();
        calcParam.myPlanePosition.set(myPlane.x, myPlane.y);
        calcParam.enemyPosition.set(x, y);
        calcParam.startVelocity.copy(nd.startVelocity);
        calcParam.startVelocityAttribute.copy(nd.startVelAttrib);
        calcParam.startAcceleration.copy(nd.startAcceleration);
        calcParam.startAccelerationAttribute.copy(nd.startAccAttrib);

        if(parentEnemy != null){

            calcParam.hasParent = true;
            calcParam.parentVelocity.copy(parentEnemy.velocity);
            calcParam.parentAcceleration.copy(parentEnemy.acceleration);
            calcParam.parentFaceOnVector.copy(parentEnemy.getUnitVectorOfFaceOn());
        }else
            calcParam.hasParent = false;

        Double2Vector newVelocity =
                EnemyCalculator.getStartVelocityWithAtrib(calcParam);

        Double2Vector newAcceleration =
                EnemyCalculator.getStartAccelerationWithAtrib(calcParam);

        velocity.copy(newVelocity);
        acceleration.copy(newAcceleration);
        homingAcceleration.copy(nd.homingAcceleration);

        if(homingAcceleration.length()!=0)	isHoming = true;
        nodeDurationFrame = nd.nodeDurationFrame;
        nodeFrameCount = 0;
    }

    protected void setGenerating(int index){

        GeneratingChild gen = myData.generator.get(index);

        genObjectID = gen.objectID;
        genStartFrame = gen.startFrame;
        genRepeat = gen.repeat;
        genIntervalFrame = gen.intervalFrame;
    }

    protected void setNodeActionAnime(int index){

        if(animeSet==null) return;

        if (animeSet.getNodeActionAnime(index) == null){ // 特定nodeでデータがあるかチェック

            animeKind = AnimationSet.AnimeKind.NORMAL;
            totalAnimeFrame = 0;
            return;
        }

        animeKind = AnimationSet.AnimeKind.NODEACTION;
        totalAnimeFrame = 0;
    }

    public void periodicalProcess(){

        if(!isInExplosion){

            if(myData.generator.size() > genIndex) checkGeneratingCount();

            if(!checkNodeDuration()){
                isInScreen=false;
                return;
            }

            if(myData.collision.size()>0 && oldDrawAngle!=drawAngle) rotateCollisionRegion();

            variableProcess();
        }
        animate();
    }

    protected void variableProcess(){

        flyAhead();
        checkScreenLimit();
    }

    protected boolean checkNodeDuration(){

        if(nodeDurationFrame < ++nodeFrameCount){

            if(myData.node.size() ==(++nodeIndex)) return false;
            setMovingNode(nodeIndex);
            setNodeActionAnime(nodeIndex);
        }
        return true;
    }

    protected void flyAhead(){

        if(isHoming) homing();

        fx += velocity.x;
        fy += velocity.y;

        velocity.plus(acceleration);

        if(isGrounder) fy += Global.scrollSpeedPerFrame;

        x = (int)fx;
        y = (int)fy;
    }

    private void homing(){

        Int2Vector myPlane = cbOfMyPlane.getMyPlanePos();

        Double2Vector vector = new Double2Vector();
        double speed = velocity.length();

        vector.set(myPlane.x - x, myPlane.y - y);
        vector.limit(speed);

        vector.x *= homingAcceleration.x;
        vector.y *= homingAcceleration.y;

        velocity.plus(vector);
        velocity.normalize(speed);
    }

    protected void checkGeneratingCount(){

        tempGenChild = null;

        if(genFrameCount++ < genStartFrame) return;

        tempGenChild = requestGenerating();

        if (--genRepeat<1) {

            if (++genIndex < myData.generator.size()) setGenerating(genIndex);
        }
        else
            genStartFrame +=genIntervalFrame;
    }

    public Enemy requestGenerating(){

        return cbOfGeneratingChild.getGeneratingChild(this);
    }

    protected void rotateCollisionRegion(){

        for(int i=0; i<myData.collision.size(); i++){

            CollisionRegion colSrc = myData.collision.get(i);
            CollisionRegion colDst = collisionRotated.get(i);

            if(colSrc.centerX==0 && colSrc.centerY==0) continue;

            double c = Math.cos(drawAngle * Global.radian);
            double s = Math.sin(drawAngle * Global.radian);

            colDst.centerX = (int)(colSrc.centerX * c - colSrc.centerY * s);
            colDst.centerY = (int)(colSrc.centerX * s + colSrc.centerY * c);
        }
        oldDrawAngle = drawAngle;
    }

    protected void animate(){

        if(animeSet==null) return;

        AnimationData anime = animeSet.getAnime(animeKind, nodeIndex);
        drawAngle = AnimationManager.getEnemyRotateAngle(anime, this, false);

        int eva = AnimationManager.checkAnimeLimit(anime, ++totalAnimeFrame);

        if (eva == -1) isInScreen = false;
        else
            animeFrame = eva;
    }

    public AnimationData getCurrentAnimeData(){

        AnimationData anime = animeSet.getAnime(animeKind, nodeIndex);
        return anime;
    }

    //DerrivativeTypeで使用のメソッド群　↓

    public void addNodeDuration(int index, int addDuration){

        MovingNode nd = myData.node.get(index);
        nd.nodeDurationFrame += addDuration;

        if(index ==0) {	// 初期nodeは再設定しないと反映されない
            setMovingNode(0);
            drawAngle = AnimationManager.getEnemyRotateAngle(animeSet.normalAnime, this, true);
        }
    }

    public void slideStartFrame(int index, int slideFrame){

        GeneratingChild gen = myData.generator.get(index);
        gen.startFrame += slideFrame;

        if(index ==0) {	// 初期generatingは再設定しないと反映されない
            setGenerating(0);
        }
    }

    public void cueingGenerating(int index, int skipFrame){

        genIndex = index;
        setGenerating(genIndex);
        genFrameCount = skipFrame;
    }

	/*final float[] shadowColor = {0, 0, 0, 0};

	public void onDrawShadow(GL10 gl){

		if(!hasShadow) return;

		InitGL.changeTexColor(shadowColor);

		drawCenter.set
			(x+Global.shadowDeflectionX, y+Global.shadowDeflectionY);

		gl.glMatrixMode(GL10.GL_MODELVIEW);

		gl.glPushMatrix();
		{
			gl.glLoadIdentity();

			gl.glTranslatef(drawCenter.x, drawCenter.y, 0);
			gl.glRotatef((float)drawAngle, 0, 0, 1);

			drawCenter.set(0, 0);
			animation.setFrame(animeSet.getData(animeKind, nodeIndex), animeFrame);
			animation.drawScaledFrame
				(drawCenter, Global.shadowScaleX, Global.shadowScaleY);
		}
		gl.glPopMatrix();

		InitGL.changeTexColor(null);
	}

	public void onDrawIfGrounder(GL10 gl){

		if(isGrounder) onDraw(gl);
	}

	public void onDrawIfAir(GL10 gl){

		if(!isGrounder) onDraw(gl);
	}

	public void onDraw(GL10 gl){

		drawCenter.set(x, y);

		gl.glMatrixMode(GL10.GL_MODELVIEW);

		gl.glPushMatrix();
		{
			gl.glLoadIdentity();

			gl.glTranslatef(drawCenter.x, drawCenter.y, 0);
			gl.glRotatef((float)drawAngle, 0, 0, 1);

			drawCenter.set(0, 0);
			animation.setFrame(animeSet.getData(animeKind, nodeIndex), animeFrame);
			animation.drawFrame(drawCenter);
		}
		gl.glPopMatrix();
	}*/
}
