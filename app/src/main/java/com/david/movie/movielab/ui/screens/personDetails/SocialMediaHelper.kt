package com.david.movie.movielab.ui.screens.personDetails

import android.content.pm.PackageManager
import android.net.Uri
import com.david.movie.notwork.dto.PersonExternalIdsTMDB

class SocialMediaHelper(
    private val externalIds: PersonExternalIdsTMDB,
    private val packageManager: PackageManager,
) {

    private fun getUriForSocialMedia(appPackage: String, webUrl: String, userId: String?): Uri? {
        userId?.let {
            // Check if the app is installed
            return if (isAppInstalled(appPackage)) {
                // Intent URI that opens the app
                Uri.parse("intent://$it#Intent;package=$appPackage;scheme=https;end")
            } else {
                // Web URL
                Uri.parse("$webUrl/$it")
            }
        }
        return null
    }

    private fun isAppInstalled(uri: String): Boolean {
        return try {
            packageManager.getPackageInfo(uri, PackageManager.GET_ACTIVITIES)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    fun getInstagramUrl(): Uri? {
        return getUriForSocialMedia(
            appPackage = "com.instagram.android",
            webUrl = "https://instagram.com",
            userId = externalIds.instagram_id
        )
    }

    fun getTikTokUrl(): Uri? {
        return getUriForSocialMedia(
            appPackage = "com.zhiliaoapp.musically",
            webUrl = "https://www.tiktok.com",
            userId = externalIds.tiktok_id
        )
    }

    fun getFacebookUrl(): Uri? {
        return getUriForSocialMedia(
            appPackage = "com.facebook.katana",
            webUrl = "https://facebook.com",
            userId = externalIds.facebook_id
        )
    }
}
