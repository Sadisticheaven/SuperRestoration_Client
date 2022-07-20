package com.example.superrestoration_client.utils

object Config {
//    const val baseUrl = "http://169.254.231.255:8181" // dormitory
//    const val webBaseUrl = "http://169.254.231.255:8080/" // dormitory
    const val baseUrl = "http://192.168.137.1:8181" // mobile
    const val webBaseUrl = "http://192.168.137.1:8080" // mobile


    //    const val baseUrl = "http://172.20.110.221:8181"
    val fragmentTag = hashMapOf(
    Pair("MainActivity", -1), Pair("ModelFragment", 0),
    Pair("CombinationFragment", 1), Pair("DatasetFragment", 2),
    Pair("ModelSelectedFragment", 3), Pair("DatasetSelectedFragment", 4),
    Pair("ResultFragment", 5), Pair("PictureFragment", 6),
    Pair("SuperRestorationActivity", 7), Pair("SingleAlgorithmFragment", 8),
    Pair("MultiAlgorithmFragment", 9), Pair("CompareFragment", 10))
}