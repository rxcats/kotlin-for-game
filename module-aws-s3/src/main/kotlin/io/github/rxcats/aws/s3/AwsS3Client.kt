package io.github.rxcats.aws.s3

import org.springframework.stereotype.Component
import software.amazon.awssdk.core.ResponseBytes
import software.amazon.awssdk.core.ResponseInputStream
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.GetObjectResponse
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response
import software.amazon.awssdk.services.s3.model.ObjectIdentifier
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import java.io.File

@Component
class AwsS3Client(
    private val s3Client: S3Client,
    private val properties: AwsS3Properties
) {

    fun getObject(keyName: String): ResponseInputStream<GetObjectResponse> {
        return s3Client.getObject {
            it.bucket(properties.bucket)
            it.key(keyName)
        }
    }

    fun getObjectAsBytes(keyName: String): ResponseBytes<GetObjectResponse> {
        return s3Client.getObjectAsBytes {
            it.bucket(properties.bucket)
            it.key(keyName)
        }
    }

    fun getObjectUrl(keyName: String): String {
        return s3Client.utilities().getUrl {
            it.bucket(properties.bucket)
            it.key(keyName)
        }.toExternalForm()
    }

    fun getObjectList(prefix: String): ListObjectsV2Response {
        return s3Client.listObjectsV2 {
            it.bucket(properties.bucket)
            it.prefix(prefix)
        }
    }

    private fun buildPutObjectRequest(keyName: String): PutObjectRequest {
        return PutObjectRequest.builder()
            .bucket(properties.bucket)
            .key(keyName)
            .build()
    }

    fun putObject(keyName: String, file: File): String {
        s3Client.putObject(buildPutObjectRequest(keyName), RequestBody.fromFile(file))
        return getObjectUrl(keyName)
    }

    fun putObject(keyName: String, fileString: String): String {
        s3Client.putObject(buildPutObjectRequest(keyName), RequestBody.fromString(fileString))
        return getObjectUrl(keyName)
    }

    fun putObject(keyName: String, fileBytes: ByteArray): String {
        s3Client.putObject(buildPutObjectRequest(keyName), RequestBody.fromBytes(fileBytes))
        return getObjectUrl(keyName)
    }

    fun deleteObject(keyName: String) {
        s3Client.deleteObject {
            it.bucket(properties.bucket)
            it.key(keyName)
        }
    }

    fun deleteObjectList(keys: List<String>) {
        if (keys.isEmpty()) return

        val objectIds = keys.map {
            ObjectIdentifier.builder().key(it).build()
        }

        s3Client.deleteObjects {
            it.bucket(properties.bucket)
            it.delete { d ->
                d.objects(objectIds)
            }
        }
    }
}
