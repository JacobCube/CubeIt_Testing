package cz.cubeit.cubeit

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_framework_properties_options.view.*

class Fragment_FrameworkPropertiesOptions : Fragment() {
    var tempView: View? = null

    companion object{
        fun newInstance(rotation: Boolean = false, animate: Boolean = false, component: SystemFlow.FrameworkComponent, maximized: Boolean): Fragment_FrameworkPropertiesOptions{
            val fragment = Fragment_FrameworkPropertiesOptions()
            val args = Bundle()
            args.putBoolean("rotation", rotation)
            args.putBoolean("animate", animate)
            args.putSerializable("component", component)
            args.putBoolean("maximized", maximized)
            fragment.arguments = args
            return fragment
        }
    }

    fun getPropertiesOptions(): SystemFlow.PropertiesOptions{
        return SystemFlow.PropertiesOptions(
                tempView?.editTextFrameworkPropertiesWidth,
                tempView?.editTextFrameworkPropertiesHeight,
                tempView?.textViewFrameworkPropertiesBringOnTop,
                tempView?.editTextFrameworkPropertiesRotation,
                tempView?.switchFrameworkPropertiesAnimate,
                tempView?.textViewFrameworkPropertiesRemove,
                tempView?.editTextFrameWorkPropertiesContent,
                tempView?.spinnerFrameWorkPropertiesFont,
                tempView?.imageViewFrameworkPropertiesBg,
                tempView?.imageViewFrameworkPropertiesPallete,
                tempView?.switchFrameworkPropertiesBackground,
                tempView?.imageViewFrameworkPropertiesPalleteBg,
                tempView?.imageViewFrameworkPropertiesWidthLock,
                tempView?.imageViewFrameworkPropertiesHeightLock
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val component = arguments?.getSerializable("component") as? SystemFlow.FrameworkComponent
        val maximized = arguments?.getBoolean("maximized", true) ?: true

        tempView = inflater.inflate(if(maximized) R.layout.fragment_framework_properties_options else R.layout.fragment_framework_properties_options_vertical, container, false)

        if(component != null){
            tempView?.editTextFrameworkPropertiesWidth?.setHTMLText((component.width.toInt()).toString())
            tempView?.editTextFrameworkPropertiesHeight?.setHTMLText((component.height.toInt()).toString())
            tempView?.editTextFrameworkPropertiesRotation?.setHTMLText((component.rotationAngle.toInt()).toString())
            tempView?.switchFrameworkPropertiesAnimate?.isChecked = component.animate
            tempView?.editTextFrameWorkPropertiesContent?.setHTMLText(component.textContent)
            tempView?.switchFrameworkPropertiesBackground?.isChecked = component.hasBackground
            tempView?.imageViewFrameworkPropertiesPalleteBg?.isEnabled = component.hasBackground
        }

        if(component?.type == SystemFlow.FrameworkComponentType.TEXT || component?.type == SystemFlow.FrameworkComponentType.DIALOG){
            tempView?.editTextFrameWorkPropertiesContent?.visibility = View.VISIBLE
            tempView?.switchFrameworkPropertiesBackground?.visibility = View.VISIBLE
            tempView?.imageViewFrameworkPropertiesPalleteBg?.visibility = View.VISIBLE
            tempView?.spinnerFrameWorkPropertiesFont?.visibility = View.VISIBLE
            tempView?.imageViewFrameworkPropertiesPallete?.visibility = View.VISIBLE
        }

        if(arguments?.getBoolean("rotation", false) == false){
            tempView?.editTextFrameworkPropertiesRotation?.visibility = View.GONE
        }
        if(arguments?.getBoolean("animate", false) == false){
            tempView?.switchFrameworkPropertiesAnimate?.visibility = View.GONE
        }

        return tempView
    }
}
