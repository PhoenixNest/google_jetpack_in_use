package com.dev.retrofit_in_use.ui.fragments.compare

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.dev.retrofit_in_use.R
import com.dev.retrofit_in_use.databinding.FragmentCompareBinding
import com.dev.retrofit_in_use.utils.Constants
import com.dev.retrofit_in_use.utils.FileUtil
import com.dev.retrofit_in_use.viewmodel.CompareViewModel
import dagger.hilt.android.scopes.FragmentScoped
import id.zelory.compressor.Compressor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.shouheng.compress.Compress
import me.shouheng.compress.concrete
import me.shouheng.compress.strategy.config.ScaleMode
import java.io.File

@FragmentScoped
class CompareFragment : Fragment() {
    private var _binding: FragmentCompareBinding? = null

    private lateinit var compareViewModel: CompareViewModel

    // Ensure that we can get the binding Layout
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentCompareBinding.inflate(inflater, container, false)

        // Set up lifecycleOwner
        binding.lifecycleOwner = this

        // Bind ViewModel
        compareViewModel = ViewModelProvider(requireActivity())[CompareViewModel::class.java]

        // Set Menu
        setHasOptionsMenu(true)

        return binding.root
    }

    /* ======================== [Top Action Bar] ======================== */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.compare_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_compare_disk -> loadImageFromDisk()
            R.id.menu_compare_camera -> shotPhoto()
        }

        return super.onOptionsItemSelected(item)
    }

    /* ======================== [Top Action Bar] - loadImageFromDisk ======================== */
    private fun loadImageFromDisk() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(intent, Constants.INTENT_ACTION_SELECT_PHOTO)
    }

    // Handle onActivityResult
    private fun handleDiskResult(data: Intent) {
        // Bind the Origin Image Data with ViewModel
        compareViewModel.imageUri = data.data

        binding.imageUri = compareViewModel.imageUri
        setUpImageView(compareViewModel.imageUri)

        // Origin Image Size
        val fileSizeFromUri = compareViewModel.getImageSizeFromUri(
            context = requireContext(),
            imageUri = compareViewModel.imageUri
        )
        setUpOriginTextView(originSizeText = fileSizeFromUri)

        compareViewModel.imageUri?.let { setUpButton(it) }
    }

    /* ======================== [Top Action Bar] - shotPicture ======================== */
    private fun shotPhoto() {
        //
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Retrieve Result Successful
        if (resultCode == Activity.RESULT_OK) {
            if (data != null) {
                when (requestCode) {
                    // Handle Select Photo
                    Constants.INTENT_ACTION_SELECT_PHOTO -> {
                        handleDiskResult(data)
                    }

                    // Handle Take Photo
                    Constants.INTENT_ACTION_TAKE_PHOTO -> {
                        shotPhoto()
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

    // Image Compress Framework: Compressor By zetbaitsu
    // https://github.com/zetbaitsu/Compressor
    private suspend fun compressImageWithCompressorByZetbaitsu(
        context: Context,
        imageUri: Uri
    ) {
        val fileByUri = FileUtil.getFileByUri(context, imageUri)

        // TODO: Code Need to be optimize
        lifecycleScope.launch {
            val compressImage = Compressor.compress(context, fileByUri)

            lifecycleScope.launch(Dispatchers.Main) {
                // Show Success Toast
                Toast.makeText(
                    requireContext(),
                    "Compress Successful, Path: ${compressImage.path}",
                    Toast.LENGTH_LONG
                ).show()

                // New Image
                val newBitmap = BitmapFactory.decodeFile(compressImage.path)
                binding.ivCompareNew.setImageBitmap(newBitmap)

                // New Image Size
                val newImageUri = Uri.parse(
                    MediaStore.Images.Media.insertImage(
                        requireContext().contentResolver,
                        newBitmap,
                        "newBitmap",
                        null
                    )
                )

                // TODO: This value is always be 0B in the Disk, but the Image Info shows the correct Value of the Image
                setUpNewTextView(
                    newSizeText = compareViewModel.getImageSizeFromUri(
                        requireContext(),
                        newImageUri
                    )
                )
            }
        }
    }

    // Image Compress Framework: Compressor By Shouheng88
    // https://github.com/Shouheng88/Compressor
    private fun compressImageWithCompressorByShouheng88(
        context: Context,
        imageUri: Uri
    ) {
        val fileByUri: File = FileUtil.getFileByUri(context, imageUri)

        lifecycleScope.launch {
            val result = context.let { context ->
                Compress.with(context, fileByUri.toURI())
                    .setQuality(80)
                    .concrete {
                        withMaxWidth(100f)
                        withMaxHeight(100f)
                        withScaleMode(ScaleMode.SCALE_HEIGHT)
                        withIgnoreIfSmaller(true)
                    }
                    .get(Dispatchers.IO)
            }

            withContext(Dispatchers.Main) {
                context.let { viewContext ->
                    Glide.with(viewContext).load(result).into(binding.ivCompareNew)
                }
            }
        }
    }

    // Image Compress Framework: SiliCompressor By Tourenathan-G5organisation
    // https://github.com/Tourenathan-G5organisation/SiliCompressor
    private fun compressImageBySiliCompressor() {
        // This Function will be removed in the Future
    }

    private fun setUpImageView(imageUri: Uri?) {
        binding.ivCompareOrigin.setImageURI(imageUri)
    }

    private fun setUpOriginTextView(originSizeText: String = "Origin Image Size: 0") {
        binding.tvCompareOrigin.text = "Origin Image Size: $originSizeText"

    }

    private fun setUpNewTextView(newSizeText: String = "New Image Size: 0") {
        binding.tvCompareNew.text = "New Image Size: $newSizeText"
    }

    private fun setUpButton(uri: Uri) {
        binding.btnCompareCompress.setOnClickListener {
            lifecycleScope.launch {
                compressImageWithCompressorByZetbaitsu(requireContext(), uri)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        // avoid OOM
        _binding = null
    }
}