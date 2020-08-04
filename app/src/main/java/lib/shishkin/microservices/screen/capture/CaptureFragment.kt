package lib.shishkin.microservices.screen.capture

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import lib.shishkin.common.ApplicationUtils
import lib.shishkin.microservices.ApplicationSingleton
import lib.shishkin.microservices.R
import lib.shishkin.microservices.action.Actions
import lib.shishkin.sl.action.ApplicationAction
import lib.shishkin.sl.action.IAction
import lib.shishkin.sl.action.handler.FragmentActionHandler
import lib.shishkin.sl.model.IModel
import lib.shishkin.sl.provider.ApplicationProvider
import lib.shishkin.sl.ui.AbsContentFragment
import java.io.IOException


class CaptureFragment : AbsContentFragment(), SwipeRefreshLayout.OnRefreshListener {

    companion object {
        const val NAME = "CaptureFragment"
        const val id = 100
        const val TakeFoto = 101
        const val SelectFoto = 102

        fun newInstance(): CaptureFragment {
            return CaptureFragment()
        }
    }

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var image: ImageView
    private lateinit var btnTakeFoto: TextView
    private lateinit var btnSelectFoto: TextView
    private val actionHandler = FragmentActionHandler(this)

    override fun createModel(): IModel {
        return CaptureModel(this)
    }

    override fun getName(): String {
        return NAME
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_capture, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout)
        swipeRefreshLayout.setColorSchemeResources(R.color.blue)
        swipeRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.gray_light)
        swipeRefreshLayout.setOnRefreshListener(this)

        btnTakeFoto = view.findViewById(R.id.takeFoto)
        btnSelectFoto = view.findViewById(R.id.selectFoto)

        image = view.findViewById(R.id.image)

        btnTakeFoto.setOnClickListener(this::onClick)
        btnSelectFoto.setOnClickListener(this::onClick)
    }

    override fun onRefresh() {
        if (swipeRefreshLayout.isRefreshing) {
            swipeRefreshLayout.isRefreshing = false
        }
        getModel<CaptureModel>().getPresenter<CapturePresenter>()
            .addAction(ApplicationAction(Actions.OnSwipeRefresh))
    }

    override fun onAction(action: IAction): Boolean {
        if (!isValid()) return false

        if (action is ApplicationAction) {
            when (action.getName()) {
                CapturePresenter.Capture -> {
                    btnSelectFoto.isEnabled = true
                    btnTakeFoto.isEnabled = true
                    return true
                }
            }
        }

        if (actionHandler.onAction(action)) return true

        ApplicationSingleton.instance.onError(
            getName(),
            "Unknown action:$action",
            true
        )
        return false
    }

    @SuppressLint("NewApi")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                TakeFoto -> {
                    val thumbnail = data?.extras?.get("data")
                    image.setImageBitmap(thumbnail as Bitmap)
                }
                SelectFoto -> {
                    if (data?.data != null) {
                        val contentURI: Uri? = data.data
                        val resolver = this.activity?.contentResolver
                        if (resolver != null && contentURI != null) {
                            try {
                                @Suppress("DEPRECATION")
                                val bitmap = when {
                                    Build.VERSION.SDK_INT < 28 -> MediaStore.Images.Media.getBitmap(
                                        resolver,
                                        contentURI
                                    )
                                    else -> {
                                        val source = ImageDecoder.createSource(
                                            resolver,
                                            contentURI
                                        )
                                        ImageDecoder.decodeBitmap(source)
                                    }
                                }
                                image.setImageBitmap(bitmap)
                            } catch (e: IOException) {
                                ApplicationSingleton.instance.onError(NAME, e)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun onClick(v: View?) {
        when (v?.id) {
            R.id.takeFoto -> {
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(intent, TakeFoto)
            }
            R.id.selectFoto -> {
                val intent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(intent, SelectFoto)
            }
        }
    }

    override fun onPermissionGranted(permission: String) {
        if (!ApplicationUtils.checkPermission(
                ApplicationProvider.appContext,
                Manifest.permission.CAMERA
            ) || !ApplicationUtils.checkPermission(
                ApplicationProvider.appContext, Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        ) {
            btnSelectFoto.isEnabled = false
            btnTakeFoto.isEnabled = false
        }
        if (ApplicationUtils.checkPermission(
                ApplicationProvider.appContext,
                Manifest.permission.CAMERA
            ) && ApplicationUtils.checkPermission(
                ApplicationProvider.appContext, Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        ) {
            btnSelectFoto.isEnabled = true
            btnTakeFoto.isEnabled = true
        }
    }

    override fun onPermissionDenied(permission: String) {
        btnSelectFoto.isEnabled = false
        btnTakeFoto.isEnabled = false
    }


}
