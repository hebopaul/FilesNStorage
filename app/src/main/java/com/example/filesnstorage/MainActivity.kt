package com.example.filesnstorage

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.filesnstorage.ui.theme.FilesNStorageTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FilesNStorageTheme {
                val viewModelFactory = MainViewModelFactory(this)
                val mVM = viewModel<MainViewModel>(factory = viewModelFactory)

                val photoPickerLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.PickVisualMedia(),
                    onResult = { uri ->
                        uri?.let {
                            mVM.updateSelectedImageUri(uri)
                            val selectedImage = BitmapFactory.decodeFile(mVM.selectedImageUri?.path)
                            if (selectedImage != null) {
                                mVM.saveImageToInternal(selectedImage, uri.lastPathSegment.toString())
                            } else {
                                Log.e("MainActivity", "Failed to decode selected image")
                            }
                        }
                    }
                )
                // A surface container using the 'background' color from the theme
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        AsyncImage(
                            model = if (mVM.selectedImageUri == null) R.drawable.ic_launcher_background
                                else mVM.selectedImageUri,
                            contentDescription = "image to be saved",
                            modifier = Modifier
                                .fillMaxHeight(0.5F)
                        )


                        Row(
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            OutlinedButton(
                                onClick = { photoPickerLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )}
                            ) {
                                Text("Import Image")
                            }

                            OutlinedButton(
                                onClick = { mVM.isFileListVisible = true}
                            ) {
                                Text("Images List")
                            }
                        }

                        if (mVM.isFileListVisible) {
                            ListFiles(mVM)
                        } else Text("Press the \"Images List\" button to see the list of images")
                    }

                }
            }
        }
    }
}

@Composable
fun ListFiles(mVM: MainViewModel) {
    LazyColumn {
        //present all the uri paths of our list
        items( mVM.getFileList().size ) { filePath ->
            Text(text = filePath.toString())
        }

    }
}