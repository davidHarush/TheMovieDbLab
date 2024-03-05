package com.david.movie.lab.ui.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.david.movie.lab.repo.model.Actor
import com.david.movie.lab.repo.model.getPosterUrl


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
            ActorAvatar(actor, onActorClick = onActorClick)
        }
    }
}


@Composable
fun ActorAvatar(actor: Actor, onActorClick: (Int) -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(12.dp)
            .clickable { onActorClick(actor.id ?: 0) }
    ) {
        Image(
            painter = rememberAsyncImagePainter(model = actor.getPosterUrl()),
            contentDescription = "Actor Image",
            modifier = Modifier
                .size(70.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = actor.name ?: "",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.LightGray,
            modifier = Modifier.fillMaxWidth()
        )
    }
}