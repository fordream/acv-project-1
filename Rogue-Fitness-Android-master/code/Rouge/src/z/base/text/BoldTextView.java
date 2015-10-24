package z.base.text;

import z.base.Fonts;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;
//z.base.text.BoldTextView
public class BoldTextView extends TextView {

	public BoldTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		init();
	}

	public BoldTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public BoldTextView(Context context) {
		super(context);
		init();
	}

	private void init() {

		Fonts.getIntance(getContext()).setFontBold(this);
	}
}
