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

import java.lang.Math;


/*
everything is immutable here.
*/
public class Vector2D {
    double x;
    double y;

    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getMagnitude() {
        return Math.sqrt(x*x + y*y);
    }
    public double getDirection() {
        return Math.atan2(y, x);
    }

    public Vector2D fromPolar(double magn, double dir) {
        return new Vector2D(magn * Math.cos(dir), magn * Math.sin(dir));
    }


    /* returns new object */
    public Vector2D rotate(double dir) {
        double magn = this.getMagnitude();
        double cdir = this.getDirection();

        return fromPolar(magn, cdir + dir);
    }

    /* also returns new object */
    public Vector2D normalize() {
        double magn = this.getMagnitude();
        return new Vector2D(x / magn, y / magn);
    }
    public Vector2D scale(double k) {
        return new Vector2D(x * k, y * k);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public String toString() {
        return "x: " + x + ", y: " + y;

    }



}
