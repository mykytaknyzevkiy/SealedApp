package com.sealed.app.screen.main

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sealed.app.R
import com.sealed.app.ui.view_holder.AppViewHolder
import com.sealed.repository.model.AppModel
import com.sealed.repository.unit.StateImp
import com.sealed.ui.them.mhpColor

@ExperimentalFoundationApi
@Composable
fun MainScreen(mViewModel: MainViewModel = viewModel()) = Surface(
    color = mhpColor.screenBackground,
    contentColor = mhpColor.primary
) {
    val context = LocalContext.current

    Image(
        modifier = Modifier.fillMaxSize(),
        painter = painterResource(id = R.drawable.main_background),
        contentDescription = null,
        contentScale = ContentScale.FillBounds
    )

    Column {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            painter = painterResource(id = R.drawable.main_screen_banner),
            contentScale = ContentScale.FillWidth,
            contentDescription = null
        )

        var vncUrl: String by remember {
            mutableStateOf(mViewModel.vncUrl(context))
        }

        TextField(
            modifier = Modifier.padding(16.dp),
            value = vncUrl,
            onValueChange = {
                it.replace(" ", "").also { n ->
                    vncUrl = n
                    mViewModel.changeVncUrl(context, vncUrl)
                }
            }
        )

        Column(modifier = Modifier.padding(16.dp).weight(1f)) {
            val gridAppsState by mViewModel.apps.collectAsState()

            if (gridAppsState is StateImp.Data)
                GridApps(apps = (gridAppsState as StateImp.Data<List<AppModel>>).value)

            /*val favouriteApps by mViewModel.favouriteApps.collectAsState()

            if (favouriteApps is StateImp.Data)
                FavouriteApps(apps = (favouriteApps as StateImp.Data<List<AppModel>>).value)*/
        }

        Image(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            painter = painterResource(id = R.drawable.main_screen_bottom_banner),
            contentScale = ContentScale.Crop,
            contentDescription = null
        )
    }

    LaunchedEffect(key1 = 1) {
        mViewModel.load(context)
    }
}

@ExperimentalFoundationApi
@Composable
fun ColumnScope.GridApps(apps: List<AppModel>) = LazyVerticalGrid(
    modifier = Modifier
        .fillMaxWidth()
        .weight(1f),
    cells = GridCells.Fixed(3)
) {
    items(apps) {
        AppViewHolder(appModel = it)
    }
}

@Composable
private fun FavouriteApps(apps: List<AppModel>) = Row(modifier = Modifier
    .fillMaxWidth()
    .background(Color.LightGray, RoundedCornerShape(25.dp))
    .padding(horizontal = 16.dp),
    verticalAlignment = Alignment.CenterVertically
) {
    apps.forEachIndexed { index, it ->
        AppViewHolder(
           // modifier = Modifier.padding(8.dp),
            appModel = it,
            isNameAvailable = false
        )

        if (index != apps.lastIndex)
            Spacer(modifier = Modifier.weight(1f))
    }
}