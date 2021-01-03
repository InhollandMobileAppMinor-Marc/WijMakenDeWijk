package nl.woonwaard.wij_maken_de_wijk.ui.core

import android.content.Context
import androidx.viewbinding.ViewBinding

val ViewBinding.context: Context
    get() = root.context
