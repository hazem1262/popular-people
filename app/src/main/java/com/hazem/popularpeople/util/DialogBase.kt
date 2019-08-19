package com.hazem.popularpeople.util

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.hazem.popularpeople.R


/**
 * Created by hazem.ashraf on 8/18/2019.
 */
open class DialogBase : DialogFragment() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setStyle(STYLE_NORMAL, R.style.fullScreenDialog)
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
		return inflater.inflate(getLayoutResource(), container)
	}


	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
		attachListeners()
	}

	override fun onActivityCreated(savedInstanceState: Bundle?) {
		super.onActivityCreated(savedInstanceState)
		dialog?.window?.attributes?.windowAnimations = R.style.DialogAnimationDownUp
	}

	open fun attachListeners() {}
	open fun getLayoutResource(): Int {
		return R.layout.dialog_error
	}


	interface DialogActions {
		fun onPositiveAction()
		fun onNegativeAction()
	}

}