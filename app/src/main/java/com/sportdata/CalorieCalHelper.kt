package com.sportdata

import com.utils.lib.ss.common.MathUtil

class CalorieCalHelper {

    companion object {

        /**
         * 计算消耗的卡路里。
         * @param meterDistance 运动的距离,以米为单位
         * @param totalSeconds 运动的时间 ，以秒为单位
         * @return 消耗的卡路里
         */
        fun getRunningCalorieByDis(meterDistance: Double, totalSeconds: Int): Double {
            val meterPerSecond = MathUtil.divide(meterDistance, totalSeconds.toDouble(), 10)
            return getRunningCalorie(meterPerSecond, totalSeconds)
        }

        /**
         * 计算消耗的卡路里。
         * @param meterPerSecond 运动速度 米/秒
         * @param totalSeconds 运动的时间 ，以秒为单位
         * @return 消耗的卡路里
         */
        private fun getRunningCalorie(meterPerSecond: Double, totalSeconds: Int): Double {
            val runningCaloriePerSecond = getSportCalorieByMeterPerSecond(meterPerSecond)
            val runningCalorie = MathUtil.multiply(totalSeconds.toDouble(), runningCaloriePerSecond)

            var age = 26
            var weight = 60.0
            val sex = 1
            var basicCaloriePerSecond = 0.0
            if (1 == sex) {
                //male
                basicCaloriePerSecond = BasicCalorie.getMaleBasicSecondsCalories(age, weight)
            } else {
                //female
                basicCaloriePerSecond = BasicCalorie.getFemaleBasicSecondsCalories(age, weight)
            }
            val basicCalorie = MathUtil.multiply(basicCaloriePerSecond, totalSeconds.toDouble())
            return MathUtil.add(runningCalorie, basicCalorie, 2)
        }

        private fun getSportCalorieByMeterPerSecond(sportType: Int, meterPerSecond: Double): Double {
            val temp = MathUtil.multiply(meterPerSecond, 3600.0)
            val speedKmPerHour = MathUtil.meter2Km(temp)
            val caloriePerHour = getSportCalorieByKmPerHour(sportType, speedKmPerHour)
            return MathUtil.divide(caloriePerHour, 3600.0)
        }

        private fun getSportCalorieByMeterPerSecond(meterPerSecond: Double): Double {
            return getSportCalorieByMeterPerSecond(0, meterPerSecond)
        }

        /**
         * 根据运动类型和速度 计算每小时消耗的卡路里。
         * @param sportType 运动类型 跑步，慢走等
         * @param speedKmPerHour 公里每小时
         * @return 每小时消耗的卡路里
         */
        private fun getSportCalorieByKmPerHour(sportType: Int, speedKmPerHour: Double): Double {
            var result = 0.0
            when (sportType) {
                0 ->
                    //跑步
                    result = getRunningCaloriesKmPerHour(speedKmPerHour)
                else -> {
                }
            }
            return result
        }

        /**
         * 根据速度计算每小时消耗的Calorie。
         * @param speedKmPerHour 速度 公里每小时
         * @return 每小时消耗的Calorie
         */
        private fun getRunningCaloriesKmPerHour(speedKmPerHour: Double): Double {
            var result = 0.0
            val level1 = MathUtil.compareTo(speedKmPerHour, 8.0)
            if (-1 == level1) {
                val temp = MathUtil.multiply(402.0, speedKmPerHour)
                result = MathUtil.divide(temp, 8.0, 2)
                return result
            }

            val level2 = MathUtil.compareTo(speedKmPerHour, 9.7)

            if ((0 == level1 || 1 == level1) && -1 == level2) {
                //			result = new BigDecimal(509).doubleValue();
                val temp = MathUtil.multiply(509.0, speedKmPerHour)
                result = MathUtil.divide(temp, 9.7, 2)
                return result
            }

            val level3 = MathUtil.compareTo(speedKmPerHour, 10.8)
            if ((0 == level2 || 1 == level2) && -1 == level3) {
                //result = new BigDecimal(603).doubleValue();
                val temp = MathUtil.multiply(603.0, speedKmPerHour)
                result = MathUtil.divide(temp, 10.8, 2)
                return result
            }

            val level4 = MathUtil.compareTo(speedKmPerHour, 11.3)
            if ((0 == level3 || 1 == level3) && -1 == level4) {
                //result = new BigDecimal(670).doubleValue();
                val temp = MathUtil.multiply(670.0, speedKmPerHour)
                result = MathUtil.divide(temp, 11.3, 2)
                return result
            }

            val level5 = MathUtil.compareTo(speedKmPerHour, 12.1)
            if ((0 == level4 || 1 == level4) && -1 == level5) {
                //result = new BigDecimal(704).doubleValue();
                val temp = MathUtil.multiply(704.0, speedKmPerHour)
                result = MathUtil.divide(temp, 12.1, 2)
                return result
            }

            val level6 = MathUtil.compareTo(speedKmPerHour, 12.9)
            if ((0 == level5 || 1 == level5) && -1 == level6) {
                //			result = new BigDecimal(771).doubleValue();
                val temp = MathUtil.multiply(771.0, speedKmPerHour)
                result = MathUtil.divide(temp, 12.9, 2)
                return result
            }

            val level7 = MathUtil.compareTo(speedKmPerHour, 14.5)
            if ((0 == level6 || 1 == level6) && -1 == level7) {
                //result = new BigDecimal(838).doubleValue();
                val temp = MathUtil.multiply(838.0, speedKmPerHour)
                result = MathUtil.divide(temp, 14.5, 2)
                return result
            }

            if (0 == level7 || 1 == level7) {
                //result = new BigDecimal(938).doubleValue();
                val temp = MathUtil.multiply(938.0, speedKmPerHour)
                result = MathUtil.divide(temp, 16.0, 2)
                return result
            }
            return result
        }

    }


}