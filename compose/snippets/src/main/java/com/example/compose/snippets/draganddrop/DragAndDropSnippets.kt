package com.example.compose.snippets.draganddrop

import android.content.ClipData
import android.content.ClipDescription
import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.draganddrop.dragAndDropSource
import androidx.compose.foundation.draganddrop.dragAndDropTarget
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draganddrop.DragAndDropEvent
import androidx.compose.ui.draganddrop.DragAndDropTarget
import androidx.compose.ui.draganddrop.DragAndDropTransferData
import androidx.compose.ui.draganddrop.mimeTypes

@RequiresApi(Build.VERSION_CODES.N)
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DragAndDropSnippet() {

    val url = ""

    // [START android_compose_drag_and_drop_1]
    Modifier.dragAndDropSource {
        detectTapGestures(onLongPress = {
            startTransfer(
                DragAndDropTransferData(
                    ClipData.newPlainText(
                        "image Url", url
                    )
                )
            )
        })
    }
    // [END android_compose_drag_and_drop_1]

    // [START android_compose_drag_and_drop_2]
    Modifier.dragAndDropSource {
        detectTapGestures(onLongPress = {
            startTransfer(
                DragAndDropTransferData(
                    ClipData.newPlainText(
                        "image Url", url
                    ), flags = View.DRAG_FLAG_GLOBAL
                )
            )
        })
    }
    // [END android_compose_drag_and_drop_2]

    // [START android_compose_drag_and_drop_3]
    val callback = remember {
        object : DragAndDropTarget {
            override fun onDrop(event: DragAndDropEvent): Boolean {
                // Parse received data
                return true
            }
        }
    }
    // [END android_compose_drag_and_drop_3]

    // [START android_compose_drag_and_drop_4]
    Modifier.dragAndDropTarget(
        shouldStartDragAndDrop = { event ->
            event.mimeTypes().contains(ClipDescription.MIMETYPE_TEXT_PLAIN)
        }, target = callback
    )
    // [END android_compose_drag_and_drop_4]

    // [START android_compose_drag_and_drop_5]
    object : DragAndDropTarget {
        override fun onStarted(event: DragAndDropEvent) {
            //When the drag event starts
        }

        override fun onEntered(event: DragAndDropEvent) {
            //When the dragged object enters the target surface
        }

        override fun onEnded(event: DragAndDropEvent) {
            //When the drag event stops
        }

        override fun onExited(event: DragAndDropEvent) {
            //When the dragged object exits the target surface
        }

        override fun onDrop(event: DragAndDropEvent): Boolean = true
    }
    // [END android_compose_drag_and_drop_5]
}