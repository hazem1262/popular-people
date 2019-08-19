package ae.government.tamm.core.widgets

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.hazem.popularpeople.R


/**
 * Created by muhammad.mohsen on 4/2/2018
 * a TextView subclass that uses an asset icon font to render icons directly instead of adding it in the XML every single time
 * and to be clear to us that this particular view is an icon
 */

class IconFontView(context: Context, attrs: AttributeSet) : TextView(context, attrs) {

	init {

		if (internalTypeFace == null)
			internalTypeFace = ResourcesCompat.getFont(this.context, R.font.tamm)

		typeface = internalTypeFace

		val iconFontViewAttributes = context.obtainStyledAttributes(attrs, R.styleable.IconFontView)

		// mirror the glyph if the mirrorRtl is true and the current layout direction is rtl
		layoutDirection = View.LAYOUT_DIRECTION_LOCALE

		// prevents the view from showing a different glyph in RTL layout (for example, the '{' glyph was interpreted as '}' in RTL)
		layoutDirection = View.LAYOUT_DIRECTION_LTR

		iconFontViewAttributes.recycle()
	}

	companion object {

		private var internalTypeFace: Typeface? = null
	}
}