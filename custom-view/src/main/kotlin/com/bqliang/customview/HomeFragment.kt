package com.bqliang.customview

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.preference.Preference
import androidx.preference.PreferenceCategory
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceGroup
import androidx.preference.PreferenceManager
import androidx.preference.PreferenceScreen
import com.bqliang.customview.anim.animation.FrameAnimWithAnimDrawableFragment
import com.bqliang.customview.anim.animation.FrameAnimWithAnimatedVectorDrawableFragment
import com.bqliang.customview.anim.animation.TweenAnimationFragment
import com.bqliang.customview.anim.animator.AdvancedValueAnimatorFragment
import com.bqliang.customview.anim.animator.AnimatorSetFragment
import com.bqliang.customview.anim.animator.ObjAnimatorFragment
import com.bqliang.customview.anim.animator.PropertyValueHolderFragment
import com.bqliang.customview.anim.animator.SimpleValueAnimatorFragment
import com.bqliang.customview.anim.animator.ViewPropertyAnimatorFragment
import com.bqliang.customview.clip.CircleAvatarViewWithClipFragment
import com.bqliang.customview.clip.SimpleClipRectViewFragment
import com.bqliang.customview.drawable.CustomDrawableFragment
import com.bqliang.customview.layout.AlwaysSquareImageViewFragment
import com.bqliang.customview.layout.CircleViewFragment
import com.bqliang.customview.layout.FlowLayoutFragment
import com.bqliang.customview.practise.MaterialEditTextPractiseFragment
import com.bqliang.customview.simpledraw.DashBoardViewFragment
import com.bqliang.customview.simpledraw.PieViewFragment
import com.bqliang.customview.text.AlignCenterTextViewFragment
import com.bqliang.customview.text.AlignEdgeTextViewFragment
import com.bqliang.customview.text.MultiLineTextViewFragment
import com.bqliang.customview.text.NewsPaperViewFragment
import com.bqliang.customview.text.SportViewFragment
import com.bqliang.customview.touch.DoubleTapPinchSpreadScalableImageViewFragment
import com.bqliang.customview.touch.DoubleTapScalableImageViewFragment
import com.bqliang.customview.touch.custom.MultiTouchFragment1
import com.bqliang.customview.touch.custom.MultiTouchFragment2
import com.bqliang.customview.touch.custom.MultiTouchFragment3
import com.bqliang.customview.touch.custom.SingleTouchFragment
import com.bqliang.customview.touch.custom.TwoPagerFragment
import com.bqliang.customview.touch.drag.DragHelperFragment
import com.bqliang.customview.touch.drag.DragListenerFragment
import com.bqliang.customview.touch.drag.DragToCollectFragment
import com.bqliang.customview.touch.drag.DragUpDownFragment
import com.bqliang.customview.transformation.BevelFlipViewFragment
import com.bqliang.customview.transformation.FlipViewFragment
import com.bqliang.customview.transformation.HalfFlipViewFragment
import com.bqliang.customview.transformation.Transformation2DFragment
import com.bqliang.customview.xfermode.CircleAvatarViewWithXfermodeFragment
import com.google.android.material.transition.MaterialSharedAxis
import kotlin.reflect.KClass

class HomeFragment : PreferenceFragmentCompat(),
    PreferenceFragmentCompat.OnPreferenceStartFragmentCallback {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceScreen = prefScreen(preferenceManager) {
            prefCategory("图形的位置与测量") {
                fragment("DashBoardView", DashBoardViewFragment::class)
                fragment("PieView", PieViewFragment::class)
            }
            prefCategory("Xfermode") {
                fragment("CircleAvatarView", CircleAvatarViewWithXfermodeFragment::class)
            }
            prefCategory("文字的测量") {
                fragment("SportView", SportViewFragment::class)
                fragment("AlignCenterTextView", AlignCenterTextViewFragment::class)
                fragment("AlignEdgeTextView", AlignEdgeTextViewFragment::class)
                fragment("MultiLineTextView", MultiLineTextViewFragment::class)
                fragment("NewsPaperView", NewsPaperViewFragment::class)
            }
            prefCategory("Clip") {
                fragment("SimpleClipRectView", SimpleClipRectViewFragment::class)
                fragment("CircleAvatarViewWithClip", CircleAvatarViewWithClipFragment::class)
            }
            prefCategory("几何变换") {
                fragment("Transformation2D", Transformation2DFragment::class)
                fragment("FlipView", FlipViewFragment::class)
                fragment("HalfFlipView", HalfFlipViewFragment::class)
                fragment("BevelFlipView", BevelFlipViewFragment::class)
            }
            prefCategory("视图动画") {
                fragment("帧动画-AnimationDrawable", FrameAnimWithAnimDrawableFragment::class)
                fragment("帧动画-AnimationVectorDrawable", FrameAnimWithAnimatedVectorDrawableFragment::class)
                fragment("补间动画", TweenAnimationFragment::class)
            }
            prefCategory("属性动画") {
                fragment("ViewPropertyAnimator", ViewPropertyAnimatorFragment::class)
                fragment("SimpleValueAnimator", SimpleValueAnimatorFragment::class)
                fragment("AdvancedValueAnimator", AdvancedValueAnimatorFragment::class)
                fragment("ObjectAnimator", ObjAnimatorFragment::class)
                fragment("AnimatorSet", AnimatorSetFragment::class)
                fragment("PropertyValuesHolder", PropertyValueHolderFragment::class)
            }
            prefCategory("自定义 Drawable") {
                fragment("CustomDrawable", CustomDrawableFragment::class)
            }
            prefCategory("自定义布局") {
                fragment("AlwaysSquareImageView", AlwaysSquareImageViewFragment::class)
                fragment("CircleView", CircleViewFragment::class)
                fragment("FlowLayout", FlowLayoutFragment::class)
            }
            prefCategory("ScalableImageView") {
                fragment("DoubleTap ScalableImageView", DoubleTapScalableImageViewFragment::class)
                fragment("DoubleTap Pinch Spread ScalableImageViewFragment", DoubleTapPinchSpreadScalableImageViewFragment::class)
            }
            prefCategory("自定义多点触控") {
                fragment("单点触控", SingleTouchFragment::class)
                fragment("接力型", MultiTouchFragment1::class)
                fragment("合作型", MultiTouchFragment2::class)
                fragment("各自为战型", MultiTouchFragment3::class)
            }
            prefCategory("ViewGroup 的触摸反馈") {
                fragment("TwoPager", TwoPagerFragment::class)
            }
            prefCategory("拖拽触摸反馈") {
                fragment("DragListener", DragListenerFragment::class)
                fragment("DragHelper", DragHelperFragment::class)
                fragment("DragToCollect", DragToCollectFragment::class)
                fragment("DragUpDown", DragUpDownFragment::class)
            }
            prefCategory("Other") {
                fragment("MaterialEditText", MaterialEditTextPractiseFragment::class)
            }
        }
    }

    override fun onPreferenceStartFragment(
        caller: PreferenceFragmentCompat,
        pref: Preference
    ): Boolean {
        val args: Bundle = pref.extras
        val fragment = parentFragmentManager.fragmentFactory.instantiate(
            requireActivity().classLoader, pref.fragment!!
        )
        fragment.arguments = args
        fragment.enterTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
        fragment.returnTransition = MaterialSharedAxis(MaterialSharedAxis.X, false)
        caller.exitTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
        caller.reenterTransition = MaterialSharedAxis(MaterialSharedAxis.X, false)

        fragment.setTargetFragment(caller, 0)
        parentFragmentManager
            .beginTransaction()
            .replace((requireView().parent as View).id, fragment)
            .addToBackStack(null)
            .commit()

        return true
    }

    private inline fun prefScreen(
        preferenceManager: PreferenceManager,
        block: PreferenceScreen.() -> Unit
    ): PreferenceScreen {
        val context = preferenceManager.context
        val screen = preferenceManager.createPreferenceScreen(context)
        screen.block()
        return screen
    }

    private inline fun PreferenceScreen.prefCategory(
        title: String,
        block: PreferenceCategory.() -> Unit
    ): PreferenceCategory {
        val category = PreferenceCategory(context)
        category.title = title
        addPreference(category)
        category.block()
        return category
    }

    private fun<T: Fragment> PreferenceGroup.fragment(title: String, `class`: KClass<T>) {
        val pref = Preference(requireContext())
        pref.title = title
        pref.fragment = `class`.qualifiedName
        addPreference(pref)
    }
}