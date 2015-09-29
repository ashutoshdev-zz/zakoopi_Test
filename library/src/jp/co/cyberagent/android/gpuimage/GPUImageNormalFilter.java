package jp.co.cyberagent.android.gpuimage;

import android.opengl.GLES20;

public class GPUImageNormalFilter extends GPUImageFilter {

	 public static final String GAMMA_FRAGMENT_SHADER = "" +
	            "varying highp vec2 textureCoordinate;\n" +
	            " \n" +
	            " uniform sampler2D inputImageTexture;\n" +
	            " uniform lowp float normal;\n" +
	            " \n" +
	            " void main()\n" +
	            " {\n" +
	            "     lowp vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);\n" +
	            "     \n" +
	            "     gl_FragColor = vec4(pow(textureColor.rgb, vec3(normal)), textureColor.w);\n" +
	            " }";


	private int mGammaLocation;
	private float mNormal;

	public GPUImageNormalFilter() {
		this(1.0f);
	}

	public GPUImageNormalFilter(final float normal) {
		super(NO_FILTER_VERTEX_SHADER, GAMMA_FRAGMENT_SHADER);
		mNormal = normal;
	}

	@Override
	public void onInit() {
		super.onInit();
		mGammaLocation = GLES20.glGetUniformLocation(getProgram(), "normal");
	}

	@Override
	public void onInitialized() {
		super.onInitialized();
		setGamma(mNormal);
	}

	public void setGamma(final float gamma) {
		mNormal = gamma;
		setFloat(mGammaLocation, mNormal);
	}

}
