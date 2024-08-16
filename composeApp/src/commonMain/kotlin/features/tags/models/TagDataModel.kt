package features.tags.models

import androidx.compose.ui.graphics.Color
import androidx.room.Entity
import androidx.room.PrimaryKey
import expensenotes.composeapp.generated.resources.Res
import expensenotes.composeapp.generated.resources.restaurant_24dp
import expensenotes.composeapp.generated.resources.tag_home
import org.jetbrains.compose.resources.StringResource

@Entity(tableName = "tag")
data class TagDataModel(
    @PrimaryKey val id: Long,
    val nameString: String = "",
    val color: String = "white",
    val reserve1: String = "",
    val reserve2: String = "",
    val reserve3: Boolean = false,
    val reserve4: Double = 0.0,
)