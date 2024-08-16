package features.tags.repository

import features.tags.models.TagDataModel

interface TagRepository {

    suspend fun getTags(): List<TagDataModel>

    suspend fun insertTag(tagModel: TagDataModel)
}