package z.base.text;

import z.base.Fonts;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;
//z.base.text.NormalTextView
public class NormalTextView extends TextView {

	public NormalTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		init();
	}

	public NormalTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public NormalTextView(Context context) {
		super(context);
		init();
	}

	private void init() {

		Fonts.getIntance(getContext()).setFontNormal(this);
	}
}
