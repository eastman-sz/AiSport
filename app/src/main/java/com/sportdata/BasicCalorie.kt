package com.sportdata

import com.utils.lib.ss.common.MathUtil
import java.math.BigDecimal

class BasicCalorie {

    companion object {

        /**
         * 根据年龄和体重，计算女性一天的基础卡路里消耗。
         * @param age 年龄
         * @param weight 体重
         * @return 一天的基础卡路里消耗
         */
        private fun getFemaleBasicDaysCalories(age: Int, weight: Double): Double {
            return getBasicsDaysCalories(1, age, weight)
        }

        /**
         * 根据年龄和体重，计算女性一天的基础卡路里消耗。
         * @param age 年龄
         * @param weight 体重
         * @return 一天的基础卡路里消耗
         */
        private fun getMaleBasicDaysCalories(age: Int, weight: Double): Double {
            return getBasicsDaysCalories(0, age, weight)
        }

        /**
         * 根据年龄和体重，计算女性一个小时的基础卡路里消耗。
         * @param age 年龄
         * @param weight 体重
         * @return 一个小时的基础卡路里消耗
         */
        fun getFemaleBasicHoursCalories(age: Int, weight: Int): Double {
            val daysCalories = getFemaleBasicDaysCalories(age, weight.toDouble())
            return MathUtil.divide(daysCalories, 24.0)
        }

        /**
         * 根据年龄和体重，计算女性一秒钟的基础卡路里消耗。
         * @param age 年龄
         * @param weight 体重
         * @return 一秒钟的基础卡路里消耗
         */
        fun getFemaleBasicSecondsCalories(age: Int, weight: Double): Double {
            val daysCalories = getFemaleBasicDaysCalories(age, weight)
            return MathUtil.divide(daysCalories, (24 * 3600).toDouble(), 8)
        }

        /**
         * 根据年龄和体重，计算男性一秒钟的基础卡路里消耗。
         * @param age 年龄
         * @param weight 体重
         * @return 一秒钟的基础卡路里消耗
         */
        fun getMaleBasicSecondsCalories(age: Int, weight: Double): Double {
            val daysCalories = getMaleBasicDaysCalories(age, weight)
            return MathUtil.divide(daysCalories, (24 * 3600).toDouble(), 8)
        }

        private fun getBasicsDaysCalories(sex: Int, age: Int, weight: Double): Double {
            var daysCalories = 0.0
            when (sex) {
                0 ->
                    //男
                    daysCalories = males(age, weight)
                1 ->
                    //女
                    daysCalories = females(age, weight)
                else -> {
                }
            }
            return daysCalories
        }

        private fun males(age: Int, weight: Double): Double {
            if (age <= 30) {
                return maleBetween18And30(weight)
            } else if (age > 30 && age <= 60) {
                return maleBetween30And60(weight)
            } else if (age > 60) {
                return maleUpper60(weight)
            }
            return 0.0
        }

        private fun maleBetween18And30(weight: Double): Double {
            val indexA = 15.2
            val indexB = 680.0
            val b1 = BigDecimal(indexA)
            val b2 = BigDecimal(weight)
            return b1.multiply(b2).add(BigDecimal(indexB)).toDouble()
        }

        private fun maleBetween30And60(weight: Double): Double {
            val indexA = 11.5
            val indexB = 830.0
            val b1 = BigDecimal(indexA)
            val b2 = BigDecimal(weight)
            return b1.multiply(b2).add(BigDecimal(indexB)).toDouble()
        }

        private fun maleUpper60(weight: Double): Double {
            val indexA = 13.4
            val indexB = 490.0
            val b1 = BigDecimal(indexA)
            val b2 = BigDecimal(weight)
            return b1.multiply(b2).add(BigDecimal(indexB)).toDouble()
        }

        private fun females(age: Int, weight: Double): Double {
            //age >= 18 && age <= 30
            if (age <= 30) {
                return femaleBetween18And30(weight)
            } else if (age > 30 && age <= 60) {
                return femaleBetween30And60(weight)
            } else if (age > 60) {
                return femaleUpper60(weight)
            }
            return 0.0
        }

        private fun femaleBetween18And30(weight: Double): Double {
            val indexA = 14.6
            val indexB = 450.0
            val b1 = BigDecimal(indexA)
            val b2 = BigDecimal(weight)
            return b1.multiply(b2).add(BigDecimal(indexB)).toDouble()
        }

        private fun femaleBetween30And60(weight: Double): Double {
            val indexA = 8.6
            val indexB = 830.0
            val b1 = BigDecimal(indexA)
            val b2 = BigDecimal(weight)
            return b1.multiply(b2).add(BigDecimal(indexB)).toDouble()
        }

        private fun femaleUpper60(weight: Double): Double {
            val indexA = 10.4
            val indexB = 600.0
            val b1 = BigDecimal(indexA)
            val b2 = BigDecimal(weight)
            return b1.multiply(b2).add(BigDecimal(indexB)).toDouble()
        }

    }

}