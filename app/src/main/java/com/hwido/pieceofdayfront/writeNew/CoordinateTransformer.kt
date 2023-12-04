package com.hwido.pieceofdayfront.writeNew

import java.lang.Math.pow
import kotlin.math.*

class CoordinateTransformer {
    private val RE = 6371.00877  // 지구 반경(km)
    private val GRID = 5.0  // 격자 간격(km)
    private val SLAT1 = 30.0  // 투영 위도1(degree)
    private val SLAT2 = 60.0  // 투영 위도2(degree)
    private val OLON = 126.0  // 기준점 경도(degree)
    private val OLAT = 38.0  // 기준점 위도(degree)
    private val XO = 43  // 기준점 X좌표(GRID)
    private val YO = 136  // 기준점 Y좌표(GRID)

    fun convertLatLonToXY(lat: Double, lon: Double): Pair<Int, Int> {
        val DEGRAD = PI / 180.0
        val re = RE / GRID
        val slat1 = SLAT1 * DEGRAD
        val slat2 = SLAT2 * DEGRAD
        val olon = OLON * DEGRAD
        val olat = OLAT * DEGRAD

        var sn = tan(PI * 0.25 + slat2 * 0.5) / tan(PI * 0.25 + slat1 * 0.5)
        sn = ln(cos(slat1) / cos(slat2)) / ln(sn)
        val sf = pow(tan(PI * 0.25 + slat1 * 0.5), sn) * cos(slat1) / sn
        val ro = re * sf / pow(tan(PI * 0.25 + olat * 0.5), sn)

        val ra = tan(PI * 0.25 + lat * DEGRAD * 0.5).let { re * sf / pow(it, sn) }
        var theta = lon * DEGRAD - olon
        if (theta > PI) theta -= 2.0 * PI
        if (theta < -PI) theta += 2.0 * PI
        theta *= sn
        val x = floor(ra * sin(theta) + XO + 0.5).toInt()
        val y = floor(ro - ra * cos(theta) + YO + 0.5).toInt()

        return Pair(x, y)
    }
}