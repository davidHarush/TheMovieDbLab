package com.david.movie.lab.ui.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.david.movie.lab.repo.model.Actor
import com.david.movie.lab.repo.model.getProfileUrl


@Composable
fun ActorsList(actors: List<Actor>, onActorClick: (Int) -> Unit) {
    LazyRow {
        itemsIndexed(actors.chunked(2)) { _, pair ->
            ActorColumn(pair, onActorClick = onActorClick)
        }
    }
}


@Composable
fun ActorColumn(actors: List<Actor>, onActorClick: (Int) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(8.dp)) {
        actors.forEach { actor ->
            CircleActorAvatar(actor, onActorClick = onActorClick)
        }
    }
}

@Composable
fun ActorGrid(actors: List<Actor>, onActorClick: (Int) -> Unit, topSpace: Dp) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3), // Explicitly setting to two columns
        contentPadding = PaddingValues(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(8.dp)
    ) {
        item { AppSpacer(height = topSpace) }
        item { AppSpacer(height = topSpace) }
        item { AppSpacer(height = topSpace) }
        items(actors.size) { index ->
            RoundedCornerActorAvatar(actor = actors[index], onActorClick = onActorClick)
        }
    }
}

@Composable
fun RoundedCornerActorAvatar(actor: Actor, onActorClick: (Int) -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(8.dp)
            .clickable { onActorClick(actor.id ?: 0) }
            .fillMaxWidth()
    ) {
        Image(
            painter = rememberAsyncImagePainter(model = actor.getProfileUrl()),
            contentDescription = "Actor Image",
            modifier = Modifier
                .size(150.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = actor.name ?: "",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge,
            color = Color.White,
            modifier = Modifier
                .padding(horizontal = 4.dp) // Add some horizontal padding for longer names
                .fillMaxWidth(),
            maxLines = 2, // Allow for a longer name to wrap
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun CircleActorAvatar(actor: Actor, onActorClick: (Int) -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(12.dp)
            .clickable { onActorClick(actor.id ?: 0) }
    ) {
        Image(
            painter = rememberAsyncImagePainter(model = actor.getProfileUrl()),
            contentDescription = "Actor Image",
            modifier = Modifier
                .size(70.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(4.dp))
        Box(modifier = Modifier.width(100.dp)) {
            Text(
                text = actor.name ?: "",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.LightGray,
                modifier = Modifier.fillMaxWidth(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}