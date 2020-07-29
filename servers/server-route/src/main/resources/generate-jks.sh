#!/usr/bin/env bash
# 生成jks密钥文件
keytool -genkey -alias gateway-jwt -keyalg RSA -keysize 2048 -keystore gateway-jwt.jks -validity 365 -keypass baosight -storepass baosight
