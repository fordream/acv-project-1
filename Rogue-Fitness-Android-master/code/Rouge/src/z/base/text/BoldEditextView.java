package z.base.text;

import z.base.Fonts;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

//z.base.text.BoldEditextView
public class BoldEditextView extends EditText {

	public BoldEditextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		init();
	}

	public BoldEditextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public BoldEditextView(Context context) {
		super(context);
		init();
	}

	private void init() {

		Fonts.getIntance(getContext()).setFontBold(this);
	}
}
