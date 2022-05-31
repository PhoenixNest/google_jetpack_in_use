package com.dev.retrofit_in_use.ui.fragments.video

import android.Manifest
import android.R.attr
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.media.MediaExtractor
import android.media.MediaFormat
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.dev.retrofit_in_use.R
import com.dev.retrofit_in_use.databinding.FragmentVideoBinding
import com.dev.retrofit_in_use.utils.Constants
import com.dev.retrofit_in_use.utils.FileUtil
import com.dev.retrofit_in_use.utils.WidgetUtil.Companion.showSnackbar
import com.google.android.material.snackbar.Snackbar


class VideoFragment : Fragment() {

    private val TAG = "Video Fragment"

    private var _binding: FragmentVideoBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Inflate the layout for this fragment
        _binding = FragmentVideoBinding.inflate(inflater, container, false)

        // Set Menu
        setHasOptionsMenu(true)

        return binding.root
    }

    /* ======================== [Top Action Bar] ======================== */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.video_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle Menu-Item Selected
        when (item.itemId) {
            R.id.menu_video_disk -> {
                // Check the Local-Storage Permission First
                onClickPermissionRequest(
                    view = binding.videoLayout,
                    requestPermission = Manifest.permission.READ_EXTERNAL_STORAGE,
                    successTitle = getString((R.string.permission_storage_granted)),
                    failureTitle = getString(R.string.permission_storage_required)
                )

                // Load Video From Disk
                loadVideoFromDisk()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    /* ======================== [Top Action Bar] - loadVideoFromDisk ======================== */
    private fun loadVideoFromDisk() {
        val intent = Intent()
        intent.type = "video/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, Constants.INTENT_ACTION_SELECT_VIDEO)
    }

    /* ======================== Permission ======================== */

    // Permission Code Simple copy from https://developer.android.com/codelabs/android-app-permissions
    // Permission Launcher
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Log.i("Permission: ", "Granted")
        } else {
            Log.i("Permission: ", "Denied")
        }
    }

    // Request Permission
    private fun onClickPermissionRequest(
        view: View,
        requestPermission: String,
        successTitle: String,
        failureTitle: String
    ) {
        when {
            // Snow Request Successful Snackbar
            ContextCompat.checkSelfPermission(  // checkSelfPermission() will check the permission if user has granted
                requireContext(),
                requestPermission
            ) == PackageManager.PERMISSION_GRANTED -> {
                view.showSnackbar(
                    view = view,
                    msg = successTitle,
                    length = Snackbar.LENGTH_INDEFINITE,
                    actionMessage = null
                ) {}
            }

            // Show Request Snackbar
            ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
                requestPermission
            ) -> {
                view.showSnackbar(
                    view = view,
                    msg = failureTitle,
                    length = Snackbar.LENGTH_INDEFINITE,
                    actionMessage = getString(R.string.ok)
                ) { requestPermissionLauncher.launch(requestPermission) }
            }

            else -> {
                // try to Request the Permission once again
                requestPermissionLauncher.launch(requestPermission)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Retrieve Result Successful
        if (resultCode == Activity.RESULT_OK) {
            if (data != null) {
                when (requestCode) {

                    // Handle Select Video
                    Constants.INTENT_ACTION_SELECT_VIDEO -> {
                        val uri: Uri? = data.data
                        Log.d(TAG, "Video Uri: $uri")

                        val videoFile = FileUtil.getVideoFileByUri(requireContext(), uri)
                        Log.d(TAG, "Video File Info: $videoFile")

                        // Before you want to play a video, you should use MediaExtractor to extract it first
                        val extractor = MediaExtractor().apply {
                            setDataSource(videoFile.toString())
                        }

                        // Get the total Track number of Video
                        val count = extractor.trackCount

                        // Get the Track Info about each Track
                        for (i in 0..count) {
                            val mediaFormat = extractor.getTrackFormat(i)
                            val info = mediaFormat.getString(MediaFormat.KEY_MIME)

                            Log.d("TAG", "Video Track Info: $info")
                        }
                    }

                }
            } else {
                // Handle No Data
                Toast.makeText(
                    requireContext(),
                    getString(R.string.error_no_data),
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            // Handle Request Code Error
            Toast.makeText(
                requireContext(),
                getString(R.string.error_request_code),
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}