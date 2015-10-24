package z.base.text;

import z.base.Fonts;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;
//z.base.text.NormalEditextView
public class NormalEditextView extends TextView {

	public NormalEditextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		init();
	}

	public NormalEditextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public NormalEditextView(Context context) {
		super(context);
		init();
	}

	private void init() {

		Fonts.getIntance(getContext()).setFontNormal(this);
	}
}
