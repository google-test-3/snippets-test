/*
 * Copyright 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

@file:Suppress("unused")

package com.example.compose.snippets.layouts

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerSnapDistance
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import coil.compose.rememberAsyncImagePainter
import com.example.compose.snippets.util.rememberRandomSampleImageUrl
import kotlin.math.absoluteValue
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/*
* Copyright 2023 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

@Composable
fun PagerExamples() {
    AutoAdvancePager(
        listOf(
            Color.Red,
            Color.Gray,
            Color.Green,
            Color.White
        )
    )
}

@Preview
@Composable
fun HorizontalPagerSample() {
    // [START android_compose_layouts_pager_horizontal_basic]
    // Display 10 items
    val pagerState = rememberPagerState(pageCount = {
        10
    })
    HorizontalPager(state = pagerState) { page ->
        // Our page content
        Text(
            text = "Page: $page",
            modifier = Modifier.fillMaxWidth()
        )
    }
    // [END android_compose_layouts_pager_horizontal_basic]
}

@Preview
@Composable
fun VerticalPagerSample() {
    // [START android_compose_layouts_pager_vertical_basic]
    // Display 10 items
    val pagerState = rememberPagerState(pageCount = {
        10
    })
    VerticalPager(state = pagerState) { page ->
        // Our page content
        Text(
            text = "Page: $page",
            modifier = Modifier.fillMaxWidth()
        )
    }
    // [END android_compose_layouts_pager_vertical_basic]
}

@Preview
@Composable
fun PagerScrollToItem() {
    Box {
        // [START android_compose_layouts_pager_scroll]
        val pagerState = rememberPagerState(pageCount = {
            10
        })
        HorizontalPager(state = pagerState) { page ->
            // Our page content
            Text(
                text = "Page: $page",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
            )
        }

        // scroll to page
        val coroutineScope = rememberCoroutineScope()
        Button(onClick = {
            coroutineScope.launch {
                // Call scroll to on pagerState
                pagerState.scrollToPage(5)
            }
        }, modifier = Modifier.align(Alignment.BottomCenter)) {
            Text("Jump to Page 5")
        }
        // [END android_compose_layouts_pager_scroll]
    }
}

@Preview
@Composable
fun PagerAnimateToItem() {
    Box {
        // [START android_compose_layouts_pager_scroll_animate]
        val pagerState = rememberPagerState(pageCount = {
            10
        })

        HorizontalPager(state = pagerState) { page ->
            // Our page content
            Text(
                text = "Page: $page",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
            )
        }

        // scroll to page
        val coroutineScope = rememberCoroutineScope()
        Button(onClick = {
            coroutineScope.launch {
                // Call scroll to on pagerState
                pagerState.animateScrollToPage(5)
            }
        }, modifier = Modifier.align(Alignment.BottomCenter)) {
            Text("Jump to Page 5")
        }
        // [END android_compose_layouts_pager_scroll_animate]
    }
}

@Preview
@Composable
fun PageChangesSample() {
    // [START android_compose_layouts_pager_notify_page_changes]
    val pagerState = rememberPagerState(pageCount = {
        10
    })

    LaunchedEffect(pagerState) {
        // Collect from the a snapshotFlow reading the currentPage
        snapshotFlow { pagerState.currentPage }.collect { page ->
            // Do something with each page change, for example:
            // viewModel.sendPageSelectedEvent(page)
            Log.d("Page change", "Page changed to $page")
        }
    }

    VerticalPager(
        state = pagerState,
    ) { page ->
        Text(text = "Page: $page")
    }
    // [END android_compose_layouts_pager_notify_page_changes]
}

@Composable
@Preview
fun PagerWithTabsExample() {
    val pages = listOf("Movies", "Books", "Shows", "Fun")
    // [START android_compose_layouts_pager_tabs]
    val pagerState = rememberPagerState(pageCount = {
        pages.size
    })

    TabRow(
        // Our selected tab is our current page
        selectedTabIndex = pagerState.currentPage,
    ) {
        // Add tabs for all of our pages
        pages.forEachIndexed { index, title ->
            Tab(
                text = { Text(title) },
                selected = pagerState.currentPage == index,
                onClick = { },
            )
        }
    }

    HorizontalPager(
        state = pagerState,
    ) { page ->
        Text("Page: ${pages[page]}")
    }
    // [END android_compose_layouts_pager_tabs]
}

@Preview
@Composable
fun PagerWithEffect() {
    // [START android_compose_layouts_pager_transformation]
    val pagerState = rememberPagerState(pageCount = {
        4
    })
    HorizontalPager(state = pagerState) { page ->
        Card(
            Modifier
                .size(200.dp)
                .graphicsLayer {
                    // Calculate the absolute offset for the current page from the
                    // scroll position. We use the absolute value which allows us to mirror
                    // any effects for both directions
                    val pageOffset = (
                        (pagerState.currentPage - page) + pagerState
                            .currentPageOffsetFraction
                        ).absoluteValue

                    // We animate the alpha, between 50% and 100%
                    alpha = lerp(
                        start = 0.5f,
                        stop = 1f,
                        fraction = 1f - pageOffset.coerceIn(0f, 1f)
                    )
                }
        ) {
            // Card content
        }
    }
    // [END android_compose_layouts_pager_transformation]
}

@Composable
@Preview
fun PagerStartPadding() {
    // [START android_compose_layouts_pager_padding_start]
    val pagerState = rememberPagerState(pageCount = {
        4
    })
    HorizontalPager(
        state = pagerState,
        contentPadding = PaddingValues(start = 64.dp),
    ) { page ->
        // page content
    }
    // [END android_compose_layouts_pager_padding_start]
}

@Preview
@Composable
fun PagerHorizontalPadding() {
    // [START android_compose_layouts_pager_padding_horizontal]
    val pagerState = rememberPagerState(pageCount = {
        4
    })
    HorizontalPager(
        state = pagerState,
        contentPadding = PaddingValues(horizontal = 32.dp),
    ) { page ->
        // page content
    }
    // [END android_compose_layouts_pager_padding_horizontal]
}

@Preview
@Composable
fun PagerEndPadding() {
    // [START android_compose_layouts_pager_padding_end]
    val pagerState = rememberPagerState(pageCount = {
        4
    })
    HorizontalPager(
        state = pagerState,
        contentPadding = PaddingValues(end = 64.dp),
    ) { page ->
        // page content
    }
    // [END android_compose_layouts_pager_padding_end]
}

@Preview
@Composable
fun PagerCustomSizes() {
    // [START android_compose_layouts_pager_custom_size]
    val pagerState = rememberPagerState(pageCount = {
        4
    })
    HorizontalPager(
        state = pagerState,
        pageSize = PageSize.Fixed(100.dp)
    ) { page ->
        // page content
    }
    // [END android_compose_layouts_pager_custom_size]
}

@Preview
@Composable
fun PagerWithTabs() {
    // [START android_compose_layouts_pager_with_tabs]
    val pagerState = rememberPagerState(pageCount = {
        4
    })
    HorizontalPager(
        state = pagerState,
    ) { page ->
        // page content
    }
    // [END android_compose_layouts_pager_with_tabs]
}
@Preview
@Composable
fun PagerIndicator() {
    Box(modifier = Modifier.fillMaxSize()) {
        // [START android_compose_pager_indicator]
        val pagerState = rememberPagerState(pageCount = {
            4
        })
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            // Our page content
            Text(
                text = "Page: $page",
            )
        }
        Row(
            Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(pagerState.pageCount) { iteration ->
                val color = if (pagerState.currentPage == iteration) Color.DarkGray else Color.LightGray
                Box(
                    modifier = Modifier
                        .padding(2.dp)
                        .clip(CircleShape)
                        .background(color)
                        .size(16.dp)
                )
            }
        }
        // [END android_compose_pager_indicator]
    }
}

// [START android_compose_autoadvancepager]
@Composable
fun AutoAdvancePager(pageItems: List<Color>, modifier: Modifier = Modifier) {
    Box(modifier = Modifier.fillMaxSize()) {
        val pagerState = rememberPagerState(pageCount = { pageItems.size })
        val pagerIsDragged by pagerState.interactionSource.collectIsDraggedAsState()

        val pageInteractionSource = remember { MutableInteractionSource() }
        val pageIsPressed by pageInteractionSource.collectIsPressedAsState()

        // Stop auto-advancing when pager is dragged or one of the pages is pressed
        val autoAdvance = !pagerIsDragged && !pageIsPressed

        if (autoAdvance) {
            LaunchedEffect(pagerState, pageInteractionSource) {
                while (true) {
                    delay(2000)
                    val nextPage = (pagerState.currentPage + 1) % pageItems.size
                    pagerState.animateScrollToPage(nextPage)
                }
            }
        }

        HorizontalPager(
            state = pagerState
        ) { page ->
            Text(
                text = "Page: $page",
                textAlign = TextAlign.Center,
                modifier = modifier
                    .fillMaxSize()
                    .background(pageItems[page])
                    .clickable(
                        interactionSource = pageInteractionSource,
                        indication = LocalIndication.current
                    ) {
                        // Handle page click
                    }
                    .wrapContentSize(align = Alignment.Center)
            )
        }

        PagerIndicator(pageItems.size, pagerState.currentPage)
    }
}
// [END android_compose_autoadvancepager]

@Preview
@Composable
private fun AutoAdvancePagerPreview() {
    val pageItems: List<Color> = listOf(
        Color.Red,
        Color.Gray,
        Color.Green,
        Color.White
    )
    AutoAdvancePager(pageItems = pageItems)
}

// [START android_compose_pagerindicator]
@Composable
fun PagerIndicator(pageCount: Int, currentPageIndex: Int, modifier: Modifier = Modifier) {
    Box(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(pageCount) { iteration ->
                val color = if (currentPageIndex == iteration) Color.DarkGray else Color.LightGray
                Box(
                    modifier = modifier
                        .padding(2.dp)
                        .clip(CircleShape)
                        .background(color)
                        .size(16.dp)
                )
            }
        }
    }
}
// [END android_compose_pagerindicator]

@Preview
@Composable
private fun PagerIndicatorPreview() {
    PagerIndicator(pageCount = 4, currentPageIndex = 1)
}

// [START android_compose_pager_custom_page_size]
private val threePagesPerViewport = object : PageSize {
    override fun Density.calculateMainAxisPageSize(
        availableSpace: Int,
        pageSpacing: Int
    ): Int {
        return (availableSpace - 2 * pageSpacing) / 3
    }
}
// [END android_compose_pager_custom_page_size]

@Preview
@Composable
private fun CustomSnapDistance() {
    // [START android_compose_pager_custom_snap_distance]
    val pagerState = rememberPagerState(pageCount = { 10 })

    val fling = PagerDefaults.flingBehavior(
        state = pagerState,
        pagerSnapDistance = PagerSnapDistance.atMost(10)
    )

    Column(modifier = Modifier.fillMaxSize()) {
        HorizontalPager(
            state = pagerState,
            pageSize = PageSize.Fixed(200.dp),
            beyondViewportPageCount = 10,
            flingBehavior = fling
        ) {
            PagerSampleItem(page = it)
        }
    }
    // [END android_compose_pager_custom_snap_distance]
}

@Composable
internal fun PagerSampleItem(
    page: Int,
    modifier: Modifier = Modifier,
) {
    Box(modifier.fillMaxSize()) {
        // Our page content, displaying a random image
        Image(
            painter = rememberAsyncImagePainter(model = rememberRandomSampleImageUrl(width = 600)),
            contentScale = ContentScale.Crop,
            contentDescription = null,
            modifier = Modifier.matchParentSize()
        )

        // Displays the page index
        Text(
            text = page.toString(),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(4.dp))
                .sizeIn(minWidth = 40.dp, minHeight = 40.dp)
                .padding(8.dp)
                .wrapContentSize(Alignment.Center)
        )
    }
}
