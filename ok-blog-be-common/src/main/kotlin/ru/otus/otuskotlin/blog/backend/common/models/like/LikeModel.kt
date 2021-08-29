package ru.otus.otuskotlin.blog.backend.common.models.like

import ru.otus.otuskotlin.blog.backend.common.models.OwnerIdModel

class LikeModel(
    var id: LikeIdModel = LikeIdModel.NONE,
    var entityType: ModelEntityType = ModelEntityType.POST,
    var targetId: String = "",
    var ownerId: OwnerIdModel = OwnerIdModel.NONE
)
