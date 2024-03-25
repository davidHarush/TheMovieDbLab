package com.david.movie.lab.ui.screens.personDetails

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.david.movie.lab.UiState
import com.david.movie.lab.main.AppRoutes
import com.david.movie.lab.repo.model.MovieItem
import com.david.movie.lab.ui.composable.ActionButton
import com.david.movie.lab.ui.composable.AppSpacer
import com.david.movie.lab.ui.composable.ScrollingContent
import com.david.movie.lab.ui.composable.SmallMovieRow
import com.david.movie.lab.ui.screens.ErrorScreen
import com.david.movie.lab.ui.screens.LoadingScreen
import com.david.movie.notwork.dto.PersonExternalIdsTMDB
import com.david.movie.notwork.dto.PersonTMDB
import com.david.movie.notwork.dto.getBirthDate
import com.david.movie.notwork.dto.getDeathDate
import java.time.LocalDate
import java.time.Period


@Composable
fun PersonDetailsScreen(
    personId: Int, navController: NavController,
    viewModel: PersonDetailViewModel = hiltViewModel(),
) {
    LaunchedEffect(personId) {
        viewModel.getPersonDetails(personId)
        viewModel.getPersonIds(personId)
        viewModel.getPersonMovies(personId)
    }

    val uiDetailsState by viewModel.personState.collectAsState()
    val personIdsState by viewModel.personIdsState.collectAsState()
    val personMovieState by viewModel.personMovieState.collectAsState()

    val personIds = if (personIdsState is UiState.Success) {
        (personIdsState as UiState.Success).data
    } else {
        PersonExternalIdsTMDB.getEmpty()
    }

    val personMovieList = if (personMovieState is UiState.Success) {
        (personMovieState as UiState.Success).data
    } else {
        emptyList()
    }


    when (val state = uiDetailsState) {
        is UiState.Loading -> LoadingScreen()
        is UiState.Success<PersonTMDB?> -> {
            val person = (uiDetailsState as UiState.Success).data
            person?.let {
                PersonDetails(
                    person = it,
                    personIds = personIds!!,
                    personMovieList = personMovieList,
                    navController = navController
                )
            } ?: run {
                ErrorScreen(message = "Person details are not available.")
            }
        }

        is UiState.Error -> ErrorScreen(message = state.exception.message ?: "Unknown Error")
    }


}

@Composable
fun PersonDetails(
    person: PersonTMDB,
    personIds: PersonExternalIdsTMDB,
    personMovieList: List<MovieItem>?, // Ensure correct import
    navController: NavController
) {
    ScrollingContent(
        backgroundImageUrl = "https://image.tmdb.org/t/p/original${person.profile_path}",
        content = {
                    Spacer(modifier = Modifier.height(400.dp)) // Placeholder for your image or any initial content
                    Text(
                        text = person.name,
                        style = MaterialTheme.typography.displaySmall,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    person.birthday?.let {
                        AgeAndLifeStatus(person)
                    }
                    Spacer(modifier = Modifier.height(8.dp))

                    SocialMediaRowButtons(personIds)
                    Spacer(modifier = Modifier.height(8.dp))


                    Biography(person)
                    Spacer(modifier = Modifier.height(8.dp))


                    SmallMovieRow(
                        movieList = personMovieList ?: listOf(),
                        title = "Movies: ",
                        onMovieClick = { movieItem ->
                            navController.navigate(
                                AppRoutes.movieDetailsRoute(
                                    movieId = movieItem.id.toString()
                                )
                            )
                        }
                    )

        }
    )
}

@Composable
fun Biography(person: PersonTMDB) {
    if (person.biography.isNotEmpty()) {
        Text(
            text = "Biography",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = person.biography,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White
        )
    }


}


@Composable
fun AgeAndLifeStatus(p: PersonTMDB) {
    val birthDay = p.getBirthDate()
    val deathDay = p.getDeathDate()

    Log.d("PersonDetails", "AgeAndLifeStatus: $birthDay $deathDay")

    val text = when {
        deathDay != null -> {
            // Person has passed away
            val ageAtDeath = Period.between(birthDay, deathDay).years
            "Died at $ageAtDeath years old"
        }

        else -> {
            // Person is alive
            val today = LocalDate.now()
            val currentAge = Period.between(birthDay, today).years
            "Age: $currentAge"
        }
    }

    Text(
        text = text,
        color = Color.White,
        style = MaterialTheme.typography.bodyLarge,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp
    )
}


@Composable
fun SocialMediaRowButtons(externalIds: PersonExternalIdsTMDB) {
    val packageManager = LocalContext.current.packageManager
    val socialMediaHelper = SocialMediaHelper(externalIds, packageManager)
    val context: Context = LocalContext.current
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.defaultMinSize()
    ) {
        externalIds.instagram_id?.let {
            SocialMediaButton(
                text = "Instagram",
                icon = Icons.Default.Favorite, // Make sure you have this icon in your project
                onClick = { openSocialMediaLink(context, socialMediaHelper.getInstagramUrl()) }
            )
        }
        externalIds.instagram_id?.let { AppSpacer(width = 8.dp) }
        externalIds.tiktok_id?.let {
            SocialMediaButton(
                text = "TikTok",
                icon = Icons.Default.Favorite, // Make sure you have this icon in your project
                onClick = { openSocialMediaLink(context, socialMediaHelper.getTikTokUrl()) }
            )
        }
        externalIds.tiktok_id?.let { AppSpacer(width = 8.dp) }
        externalIds.facebook_id?.let {
            SocialMediaButton(
                text = "Facebook",
                icon = Icons.Default.Favorite, // Make sure you have this icon in your project
                onClick = { openSocialMediaLink(context, socialMediaHelper.getFacebookUrl()) }
            )
        }
    }
}

@Composable
fun SocialMediaButton(text: String, icon: ImageVector, onClick: () -> Unit) {
    ActionButton(
        text = text,
        backgroundColor = Color(0xFF3F51B5).copy(alpha = 0.5f), // Or any other color you want
        onClick = onClick
    )
}


fun openSocialMediaLink(context: Context, uri: Uri?) {
    uri?.let {
        val intent = Intent(Intent.ACTION_VIEW, uri).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        try {
            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(uri.toString())))
        }
    }
}
