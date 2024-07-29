package com.sparta.bookflex.domain.photoimage.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QPhotoImage is a Querydsl query type for PhotoImage
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPhotoImage extends EntityPathBase<PhotoImage> {

    private static final long serialVersionUID = 1814531411L;

    public static final QPhotoImage photoImage = new QPhotoImage("photoImage");

    public final com.sparta.bookflex.common.utill.QTimestamped _super = new com.sparta.bookflex.common.utill.QTimestamped(this);

    public final NumberPath<Long> bookId = createNumber("bookId", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath fileName = createString("fileName");

    public final StringPath filePath = createString("filePath");

    public final NumberPath<Integer> fileSize = createNumber("fileSize", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public QPhotoImage(String variable) {
        super(PhotoImage.class, forVariable(variable));
    }

    public QPhotoImage(Path<? extends PhotoImage> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPhotoImage(PathMetadata metadata) {
        super(PhotoImage.class, metadata);
    }

}

