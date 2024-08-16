package features.tags.repository

import data.database.AppDatabase
import features.tags.models.TagDataModel

class TagRepositoryImpl(
    private val appDatabase: AppDatabase
) : TagRepository {

    override suspend fun getTags(): List<TagDataModel> = appDatabase.getTagDao().getAll()

    override suspend fun insertTag(tagModel: TagDataModel) {
        appDatabase.getTagDao().insert(tagModel)
    }
}