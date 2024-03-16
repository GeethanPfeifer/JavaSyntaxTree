/*
    This file is part of JavaSyntaxTree.
    Copyright (C) 2024 Geethan Pfeifer

    JavaSyntaxTree is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 3 of the License, or
    (at your option) any later version.

    JavaSyntaxTree is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/

package org.javasyntaxtree;

import java.awt.geom.*;

/*
Builds curve from three points.
*/
public class ThreePointCurve{
    QuadCurve2D curve;

    double x0, y0, xc, yc, x2, y2;





    public ThreePointCurve(double x0, double y0,
                           double x1, double y1,
                           double x2, double y2) {

        /*
        Formula for quadratic Bezier curve:
        X = (1-t)^2 X_0 + 2t(1-t) X_C + t^2 X_2, 0 <= t <= 1.

        Minimise "imbalance" between X_0 and X_2: t = 1/2.

        We want X_1 = X(1/2).
        X_1 = 1/4 X_0 + 1/2 X_C + 1/4 X_2.
        X_C = 2(X_1 - 1/4X_0 -1/4 X_2).
        */

        curve = new QuadCurve2D.Double(x0, y0,
                2*(x1 - 0.25*x0 - 0.25*x2), 2*(y1 - 0.25*y0 - 0.25*y2),
                x2, y2);

        this.x0 = x0;
        this.y0 = y0;
        this.xc = 2*(x1 - 0.25*x0 - 0.25*x2);
        this.yc = 2*(y1 - 0.25*y0 - 0.25*y2);
        this.x2 = x2;
        this.y2 = y2;


    }

    public QuadCurve2D getCurve() {
        return curve;
    }

    public Vector2D gradient(double t) {
        /*
        X' = (2t - 2) X_0 + 2X_C - 4t X_C + 2t X_2
        */
        return new Vector2D(
                (2*t - 2)*x0 + 2*xc - 4*t*xc + 2*t*x2,
                (2*t - 2)*y0 + 2*yc - 4*t*yc + 2*t*y2);

    }
}
