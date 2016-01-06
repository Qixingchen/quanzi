package com.tizi.quanzi.tool;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.annotation.IntRange;
import android.support.annotation.Size;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Created by qixingchen on 16/1/6.
 * 用成员头像绘制组群头像
 */
public class DrawGroupFace {

    static final int GROUP_FACE_SIZE = 900;
    static final int START_SPACE_SIZE = 60;
    static final int MIDDEN_SPACE_SIZE = 10;
    static final int BACKGROUND_COLOR = 0xAA66ccff;

    public static Observable<Bitmap> DrawGroupFace(@Size(min = 1, max = 9) final Bitmap[] face) {
        return Observable.create(new Observable.OnSubscribe<Bitmap>() {

            @Override
            public void call(Subscriber<? super Bitmap> subscriber) {
                Bitmap bitmap = Bitmap.createBitmap(GROUP_FACE_SIZE, GROUP_FACE_SIZE, Bitmap.Config.RGB_565);
                Canvas canvas = new Canvas(bitmap);
                canvas.drawColor(BACKGROUND_COLOR);
                int halfSize = GROUP_FACE_SIZE / 2;
                int oneThirdSize = GROUP_FACE_SIZE / 3;
                int size = getSize(face.length);
                switch (face.length) {
                    case 1:
                        canvas.drawBitmap(face[0], null,
                                new Rect(START_SPACE_SIZE, START_SPACE_SIZE, GROUP_FACE_SIZE - START_SPACE_SIZE,
                                        GROUP_FACE_SIZE - START_SPACE_SIZE), null);

                        break;
                    case 2:
                        canvas.drawBitmap(face[0], null,
                                new Rect(START_SPACE_SIZE, START_SPACE_SIZE, halfSize - MIDDEN_SPACE_SIZE,
                                        halfSize - MIDDEN_SPACE_SIZE), null);
                        canvas.drawBitmap(face[1], null,
                                new Rect(halfSize + MIDDEN_SPACE_SIZE, halfSize + MIDDEN_SPACE_SIZE,
                                        GROUP_FACE_SIZE - START_SPACE_SIZE, GROUP_FACE_SIZE - START_SPACE_SIZE), null);

                        break;

                    case 3:
                        canvas.drawBitmap(face[0], null,
                                new Rect((halfSize + START_SPACE_SIZE) / 2, START_SPACE_SIZE,
                                        halfSize + (halfSize - START_SPACE_SIZE) / 2,
                                        halfSize - MIDDEN_SPACE_SIZE), null);
                        canvas.drawBitmap(face[1], null,
                                new Rect(START_SPACE_SIZE, halfSize + MIDDEN_SPACE_SIZE,
                                        halfSize - MIDDEN_SPACE_SIZE, GROUP_FACE_SIZE - START_SPACE_SIZE), null);
                        canvas.drawBitmap(face[2], null,
                                new Rect(halfSize + MIDDEN_SPACE_SIZE, halfSize + MIDDEN_SPACE_SIZE,
                                        GROUP_FACE_SIZE - START_SPACE_SIZE, GROUP_FACE_SIZE - START_SPACE_SIZE), null);

                        break;
                    case 4:
                        canvas.drawBitmap(face[0], null,
                                new Rect(START_SPACE_SIZE, START_SPACE_SIZE,
                                        halfSize - MIDDEN_SPACE_SIZE, halfSize - MIDDEN_SPACE_SIZE), null);
                        canvas.drawBitmap(face[1], null,
                                new Rect(halfSize + MIDDEN_SPACE_SIZE, START_SPACE_SIZE,
                                        GROUP_FACE_SIZE - START_SPACE_SIZE, halfSize - MIDDEN_SPACE_SIZE), null);
                        canvas.drawBitmap(face[2], null,
                                new Rect(START_SPACE_SIZE, halfSize + MIDDEN_SPACE_SIZE,
                                        halfSize - MIDDEN_SPACE_SIZE, GROUP_FACE_SIZE - START_SPACE_SIZE), null);
                        canvas.drawBitmap(face[3], null,
                                new Rect(halfSize + MIDDEN_SPACE_SIZE, halfSize + MIDDEN_SPACE_SIZE,
                                        GROUP_FACE_SIZE - START_SPACE_SIZE, GROUP_FACE_SIZE - START_SPACE_SIZE), null);

                        break;
                    case 5:
                        int startX = halfSize - MIDDEN_SPACE_SIZE * 2 - size;
                        int startY = START_SPACE_SIZE + size / 2;
                        canvas.drawBitmap(face[0], null,
                                new Rect(startX, startY,
                                        startX + size, startY + size), null);

                        startX = halfSize + MIDDEN_SPACE_SIZE;
                        canvas.drawBitmap(face[1], null,
                                new Rect(startX, startY,
                                        startX + size, startY + size), null);

                        startX = START_SPACE_SIZE;
                        startY = GROUP_FACE_SIZE - START_SPACE_SIZE - size * 3 / 2;
                        canvas.drawBitmap(face[2], null,
                                new Rect(startX, startY,
                                        startX + size, startY + size), null);

                        startX += MIDDEN_SPACE_SIZE * 2 + size;
                        canvas.drawBitmap(face[3], null,
                                new Rect(startX, startY,
                                        startX + size, startY + size), null);

                        startX += MIDDEN_SPACE_SIZE * 2 + size;
                        canvas.drawBitmap(face[4], null,
                                new Rect(startX, startY,
                                        startX + size, startY + size), null);

                        break;
                    case 6:
                        startX = START_SPACE_SIZE;
                        startY = START_SPACE_SIZE + size / 2;
                        canvas.drawBitmap(face[0], null,
                                new Rect(startX, startY,
                                        startX + size, startY + size), null);

                        startX += MIDDEN_SPACE_SIZE * 2 + size;
                        canvas.drawBitmap(face[1], null,
                                new Rect(startX, startY,
                                        startX + size, startY + size), null);

                        startX += MIDDEN_SPACE_SIZE * 2 + size;
                        canvas.drawBitmap(face[2], null,
                                new Rect(startX, startY,
                                        startX + size, startY + size), null);

                        startX = START_SPACE_SIZE;
                        startY += MIDDEN_SPACE_SIZE * 2 + size;
                        canvas.drawBitmap(face[3], null,
                                new Rect(startX, startY,
                                        startX + size, startY + size), null);

                        startX += MIDDEN_SPACE_SIZE * 2 + size;
                        canvas.drawBitmap(face[4], null,
                                new Rect(startX, startY,
                                        startX + size, startY + size), null);

                        startX += MIDDEN_SPACE_SIZE * 2 + size;
                        canvas.drawBitmap(face[5], null,
                                new Rect(startX, startY,
                                        startX + size, startY + size), null);

                        break;
                    case 9:

                        startX = START_SPACE_SIZE;
                        startX += MIDDEN_SPACE_SIZE * 2 + size;
                        startX += MIDDEN_SPACE_SIZE * 2 + size;
                        startY = START_SPACE_SIZE;
                        startY += MIDDEN_SPACE_SIZE * 2 + size;
                        startY += MIDDEN_SPACE_SIZE * 2 + size;
                        canvas.drawBitmap(face[8], null,
                                new Rect(startX, startY,
                                        startX + size, startY + size), null);

                    case 8:

                        startX = START_SPACE_SIZE;
                        startX += MIDDEN_SPACE_SIZE * 2 + size;
                        startY = START_SPACE_SIZE;
                        startY += MIDDEN_SPACE_SIZE * 2 + size;
                        startY += MIDDEN_SPACE_SIZE * 2 + size;
                        canvas.drawBitmap(face[7], null,
                                new Rect(startX, startY,
                                        startX + size, startY + size), null);
                    case 7:

                        startX = START_SPACE_SIZE;
                        startY = START_SPACE_SIZE;
                        canvas.drawBitmap(face[0], null,
                                new Rect(startX, startY,
                                        startX + size, startY + size), null);

                        startX += MIDDEN_SPACE_SIZE * 2 + size;
                        canvas.drawBitmap(face[1], null,
                                new Rect(startX, startY,
                                        startX + size, startY + size), null);

                        startX += MIDDEN_SPACE_SIZE * 2 + size;
                        canvas.drawBitmap(face[2], null,
                                new Rect(startX, startY,
                                        startX + size, startY + size), null);

                        startX = START_SPACE_SIZE;
                        startY += MIDDEN_SPACE_SIZE * 2 + size;
                        canvas.drawBitmap(face[3], null,
                                new Rect(startX, startY,
                                        startX + size, startY + size), null);

                        startX += MIDDEN_SPACE_SIZE * 2 + size;
                        canvas.drawBitmap(face[4], null,
                                new Rect(startX, startY,
                                        startX + size, startY + size), null);

                        startX += MIDDEN_SPACE_SIZE * 2 + size;
                        canvas.drawBitmap(face[5], null,
                                new Rect(startX, startY,
                                        startX + size, startY + size), null);
                        startX = START_SPACE_SIZE;
                        startY += MIDDEN_SPACE_SIZE * 2 + size;
                        canvas.drawBitmap(face[5], null,
                                new Rect(startX, startY,
                                        startX + size, startY + size), null);

                        break;
                    default:
                }
                subscriber.onNext(bitmap);
            }
        })
                .subscribeOn(Schedulers.io());
    }

    public static int getSize(@IntRange(from = 1, to = 9) int length) {
        switch (length) {
            case 1:
                return GROUP_FACE_SIZE - 2 * START_SPACE_SIZE;
            case 2:
            case 3:
            case 4:
                int size = GROUP_FACE_SIZE - 2 * START_SPACE_SIZE - 2 * MIDDEN_SPACE_SIZE;
                return size / 2;
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
                size = GROUP_FACE_SIZE - 2 * START_SPACE_SIZE - 4 * MIDDEN_SPACE_SIZE;
                return size / 3;
            default:
                return getSize(2);
        }
    }
}
