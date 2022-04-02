package com.sealed.app.screen.main

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.sealed.app.R

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainScreen() = Column {
    Image(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
        painter = painterResource(id = R.drawable.main_screen_banner),
        contentScale = ContentScale.Crop,
        contentDescription = null
    )
    
    LazyVerticalGrid(
        modifier = Modifier.fillMaxWidth().weight(1f),
        cells = GridCells.Fixed(3)
    ) {

    }
    
}