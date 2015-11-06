package com.peterwang.toys.mapdraw;

import java.util.List;

/**
 * 地图操作类
 *
 * @author peter_wang
 * @create-time 15/11/4 16:27
 */
public class MapUtils {
    private static double DOUBLEOFFSET = 1.0e-10;

    public static boolean IsPointInPolygon(double x, double y,    // 点坐标
                                           List<ST_DPoint> polygonPt    // Polygon点坐标
    )                // Polygon的点数
    {
        int iLoop1;
        int iLoop2;
        /** 统计总的交点个数	*/
        long lCount = 0;
        /** 保存临时线段		*/
        ST_DPoint stDPoint1 = new ST_DPoint();
        ST_DPoint stDPoint2 = new ST_DPoint();
        /** 单个线段的交点个数	*/
        int iGetRet = 0;

        ST_DPoint stPointTmp1;
        ST_DPoint stPointTmp2;
        /** 遍历Polygon的各条边*/
        for (iLoop1 = 0; iLoop1 < polygonPt.size(); iLoop1++) {
            stPointTmp1 = polygonPt.get(iLoop1);
            /** 平移顶点*/
            stDPoint1.SetX(stPointTmp1.GetX() - x);
            stDPoint1.SetY(stPointTmp1.GetY() - y);
            /** 顶点在Polygon上*/
            if (Math.abs(stDPoint1.GetX()) <= DOUBLEOFFSET &&
                    Math.abs(stDPoint1.GetY()) <= DOUBLEOFFSET) {
                /** 返回顶点在Polygon上*/
                return true;
            }
            /** 取下一个顶点*/
            iLoop2 = (iLoop1 + 1) % polygonPt.size();
            stPointTmp2 = polygonPt.get(iLoop2);
            /** 平移顶点*/
            stDPoint2.SetX(stPointTmp2.GetX() - x);
            stDPoint2.SetY(stPointTmp2.GetY() - y);
            /** 顶点在Polygon上*/
            if (Math.abs(stDPoint2.GetX()) <= DOUBLEOFFSET &&
                    Math.abs(stDPoint2.GetY()) <= DOUBLEOFFSET) {
                /** 返回顶点在Polygon上*/
                return true;
            }

            if (Math.abs(stDPoint1.GetY()) <= DOUBLEOFFSET &&
                    Math.abs(stDPoint2.GetY()) <= DOUBLEOFFSET) {
                if ((stDPoint1.GetX() * stDPoint2.GetX()) < 0.0) {
                    /** 返回顶点在Polygon上*/
                    return true;
                }
                /** 修改Y值*/
                stDPoint1.SetY(stDPoint1.GetY() + DOUBLEOFFSET);
                stDPoint2.SetY(stDPoint2.GetY() + DOUBLEOFFSET);
            } else if (Math.abs(stDPoint1.GetY()) <= DOUBLEOFFSET) {
                /** 修改Y1值*/
                stDPoint1.SetY(stDPoint1.GetY() + DOUBLEOFFSET);
            } else if (Math.abs(stDPoint2.GetY()) <= DOUBLEOFFSET) {
                /** 修改Y2值*/
                stDPoint2.SetY(stDPoint2.GetY() + DOUBLEOFFSET);
            }
            /** 取得交点*/
            iGetRet = IsCross(stDPoint1, stDPoint2);
            /** 线段重合*/
            if (iGetRet == 2) {
                /** 返回顶点在Polygon上*/
                return true;
            } else if (iGetRet == 1)    /** 只有一个交点*/ {
                lCount++;
            }
        }
        /** 判断交点个数是否奇数个*/
        if (lCount % 2 != 0) {
            return true;    // 内
        } else {
            return false;    // 外

        }
    }

    /*==< 函数头     >======================================================================*
    *    函数名称	      : IsCross															*
	*    功能概述	      : 取得线段与 Y = 0 正方向的交点个数								*
	*    输入参数         : const	ST_DPOINT &a_stDPoint1	: 坐标点						*
	*			          : const	ST_DPOINT &a_stDPoint2	: 坐标点						*
	*    输出参数		  :																	*
	*    返回值	          : 0					: 无交点					 				*
	*                     : 1					: 1个交点									*
	*                     : 2					: 重合										*
	*=======================================================================================*/
    private static int IsCross(ST_DPoint a_stDPoint1,    /**    线段的一个端点	 */
                               ST_DPoint a_stDPoint2    /**    线段的另一个端点 */
    ) {
        /** 定义返回值*/
        int iRet = 0;
        /** 定义线段的斜率*/
        double dK;
        /** 定义临时的X坐标值*/
        double dX;

        /** 无交点*/
        if ((a_stDPoint1.GetY() * a_stDPoint2.GetY()) > 0.0) {
            return iRet;
        }
        /** 斜率不存在*/
        if (Math.abs(a_stDPoint1.GetX() - a_stDPoint2.GetX()) <= DOUBLEOFFSET) {
            /** 无交点*/
            if (a_stDPoint1.GetX() < 0.0) {
                return iRet;
            } else if (Math.abs(a_stDPoint1.GetX()) <= DOUBLEOFFSET)    /** 重合 */ {
                iRet = 2;
                return iRet;
            } else    /** 1个交点*/ {
                iRet = 1;
                return iRet;
            }
        }
        /** 计算斜率*/
        dK = (a_stDPoint2.GetY() - a_stDPoint1.GetY()) / (a_stDPoint2.GetX() - a_stDPoint1.GetX());
        dX = a_stDPoint1.GetX() - a_stDPoint1.GetY() / dK;

        if (Math.abs(dX) <= DOUBLEOFFSET) {
            iRet = 2;
        } else if (dX > 0.0) {
            iRet = 1;
        }
        return iRet;
    }
}
