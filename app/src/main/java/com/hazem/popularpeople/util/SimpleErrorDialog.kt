package ae.government.tamm.components.shared.dialogs

import android.os.Bundle
import com.hazem.popularpeople.R
import com.hazem.popularpeople.util.DialogBase
import kotlinx.android.synthetic.main.dialog_error.*


/**
 * Created by caroline.armia on 4/30/2018.
 */

open class SimpleErrorDialog: DialogBase() {

	private var messageId = R.string.errorDialogMessage
	private var msg: String? = null
	private var buttonTextId = R.string.retry

	private var action: (() -> Unit)? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		isCancelable = false
	}

	override fun attachListeners() {
		super.attachListeners()

		if (this.msg != null){
			textViewErrorMessage.text = msg
		} else {
			textViewErrorMessage.text = getString(messageId)
		}
		okButton.text = getString(buttonTextId)

	    okButton.setOnClickListener {
		    action?.invoke()
		    dialog?.dismiss()
	    }

	    buttonCancel.setOnClickListener {
		    dialog?.dismiss()
	    }
    }
	override fun getLayoutResource(): Int {
		return R.layout.dialog_error
	}

	open fun setAction(positiveAction: (() -> Unit)?) {
		action = positiveAction
	}

	open fun setUi(errorMsg: String, btnTxtId: Int, act: (() -> Unit)?) {
		msg = errorMsg
		buttonTextId = btnTxtId
		action = act
	}

}
