package com.dev.retrofit_in_use.ui.fragments.compare

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.dev.retrofit_in_use.R
import com.dev.retrofit_in_use.databinding.FragmentCompareBinding
import com.dev.retrofit_in_use.utils.FileUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.shouheng.compress.Compress
import me.shouheng.compress.concrete
import me.shouheng.compress.strategy.config.ScaleMode
import java.io.File


class CompareFragment : Fragment() {
    private var _binding: FragmentCompareBinding? = null

    // Ensure that we can get the binding Layout
    private val binding get() = _binding!!

    private val selectPhoto = 100

    private val takePhoto = 200

    private var imageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentCompareBinding.inflate(inflater, container, false)

        // Set up lifecycleOwner
        binding.lifecycleOwner = this

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
    fun loadImageFromDisk() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(intent, selectPhoto)
    }

    // Get File Size
    private fun handleFileSizeFromUri(imageUri: Uri?): String {
        val cursor = imageUri?.let {
            context?.contentResolver?.query(
                it,
                null,
                null,
                null,
                null
            )
        }

        return if (cursor != null && cursor.moveToFirst()) {
            val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
            if (!cursor.isNull(sizeIndex)) {
                val length = cursor.getLong(sizeIndex)
                length.toString()
            } else {
                "0"
            }
        } else {
            "0"
        }
    }

    /* ======================== [Top Action Bar] - shotPicture ======================== */
    private fun shotPhoto() {
        //
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Retrieve Result Successful
        if (resultCode == Activity.RESULT_OK && data != null) {
            when (requestCode) {
                selectPhoto -> {
                    // Origin Image
                    imageUri = data.data

                    binding.imageUri = imageUri
                    setUpImageView(imageUri)

                    // Origin Image Size
                    val fileSizeFromUri = handleFileSizeFromUri(imageUri)
                    setUpTextView(fileSizeFromUri)

                    imageUri?.let { setUpButton(it) }
                }

                takePhoto -> {
                    shotPhoto()
                }

                else -> {}
            }
        } else {
            Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
        }
    }

    // Image Compress Framework: Compressor By zetbaitsu
    // https://github.com/zetbaitsu/Compressor
    private fun compressImageWithCompressorByZetbaitsu(context: Context, imageUri: Uri) {
        val fileByUri = FileUtil.getFileByUri(context, imageUri)

    }

    // Image Compress Framework: Compressor By Shouheng88
    // https://github.com/Shouheng88/Compressor
    private fun compressImageWithCompressorByShouheng88(context: Context, imageUri: Uri) {
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

    }

    private fun setUpImageView(imageUri: Uri?) {
        binding.ivCompareOrigin.setImageURI(imageUri)
    }

    private fun setUpTextView(text: String) {
        binding.tvCompareOrigin.text = "Origin Image Size: $text"
    }

    private fun setUpButton(uri: Uri) {
        binding.btnCompareCompress.setOnClickListener {
            compressImageWithCompressorByShouheng88(requireContext(), uri)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        // avoid OOM
        _binding = null
    }
}