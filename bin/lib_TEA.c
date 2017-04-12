#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>
#include <jni.h>
#include "TEA.h"

void decrypt (int *v, int *k);
void encrypt (int *v, int *k);

JNIEXPORT void JNICALL Java_TEA_nativeEncrypt
(JNIEnv *env,jobject callingObject, jintArray valueJava, jintArray keyJava) {
  // printf("encrypt started\n");
  jsize value_length = (*env)->GetArrayLength(env, valueJava);
  jint *value_elements = (*env)->GetIntArrayElements(env, valueJava, 0);

  jsize key_length = (*env)->GetArrayLength(env, keyJava);
  jint *key_elements = (*env)->GetIntArrayElements(env, keyJava, 0);

  // encrypts everything in groups of two bytes, starting at the last 2
  int i;
  for(i=0;i<value_length-1;i++){
    encrypt(&value_elements[i],key_elements);
  }

  (*env)->ReleaseIntArrayElements(env, valueJava, value_elements, 0);
  (*env)->ReleaseIntArrayElements(env, keyJava, key_elements, 0);

  // printf("encrypt ended\n");
  return;
}

JNIEXPORT void JNICALL Java_TEA_nativeDecrypt
(JNIEnv *env,jobject callingObject, jintArray valueJava, jintArray keyJava) {
  // printf("decrypt stared\n");
  jsize value_length = (*env)->GetArrayLength(env, valueJava);
  jint *value_elements = (*env)->GetIntArrayElements(env, valueJava, 0);

  jsize key_length = (*env)->GetArrayLength(env, keyJava);
  jint *key_elements = (*env)->GetIntArrayElements(env, keyJava, 0);

  // decrypts everything in groups of two bytes, starting at the last 2
  int i;
  for(i=value_length-2;i>=0;i--){
    decrypt(&value_elements[i],key_elements);
  }

  (*env)->ReleaseIntArrayElements(env, valueJava, value_elements, 0);
  (*env)->ReleaseIntArrayElements(env, keyJava, key_elements, 0);
  // printf("decrypt ended\n");
  return;
}
