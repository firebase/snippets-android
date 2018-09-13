package com.google.firebase.referencecode.storage.interfaces;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface StorageActivityInterface {

    void includesForCreateReference();

    void includesForUploadFiles() throws FileNotFoundException;

    void includesForDownloadFiles() throws IOException;

    void includesForFileMetadata();

    void includesForMetadata_delete();

    void includesForMetadata_custom();

    void includesForDeleteFiles();

    void nonDefaultBucket();

    void customApp();

}
