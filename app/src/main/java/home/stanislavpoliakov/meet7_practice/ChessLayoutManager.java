package home.stanislavpoliakov.meet7_practice;

import android.graphics.Color;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

public class ChessLayoutManager extends RecyclerView.LayoutManager {
    private static final String TAG = "chessLayoutManager_logs";
    private int leftItemBorder, rightItemBorder, topItemBorder, bottomItemBorder;
    private int firstItemOnScreen, lastItemOnScreen, itemsBeginAt = 0, lastIndex;
    private boolean alignRight = false;

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        /**
         * Вычисляем размер области отоборажения. То есть размер области RecyclerView. В дальнейшем
         * мы будем изменять параметры верхней, нижней, левой и правой границ для отображения элементов
         * (View) в рамках нашего LayoutManager'-а согласно "шахматной" доски.
         */
        leftItemBorder = getPaddingLeft(); // Левая граница области отображения с отступом.
        rightItemBorder = getWidth() - getPaddingRight(); // Ширина области отображения минус отступ
        topItemBorder = getPaddingTop(); // Верхняя граница области отображения с отступом.
        bottomItemBorder = getHeight() - getPaddingBottom();
        //Log.d(TAG, "onLayoutChildren: ");
        
        /**
         * При вызове onLayoutChildren (когда изменилось содержимое списка отображения)
         * открепляем View и сохранеяем их в кэше Scrap-heap
         */
        detachAndScrapAttachedViews(recycler);

        final int count = state.getItemCount(); // Количество элементов для отображения

        // Проходим по всем элементам списка.
        // top = bottom - закрепляем верхнюю границу следующего элемента на нижней границе
        // предыдущего, в момент завершения итерации списка (инкрементирования счетчика i++)
        for (int i = 0; i < count; i++, topItemBorder = bottomItemBorder) {
            View view = recycler.getViewForPosition(i); // Получаем View из списка

            /**
             * Меняем параметры Layout'-ов для View, чтобы они корректно отображалаись на половине экрана
             */
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            layoutParams.width = rightItemBorder / 2;
            view.setLayoutParams(layoutParams);

            addView(view,i); // Добавляем View на позицию отображения
            measureChildWithMargins(view, 0, 0); //вычисляем размеры View
            bottomItemBorder = topItemBorder + getDecoratedMeasuredHeight(view); // устанавливаем нижнюю границу области в
            // новое значение, увеличенное на померянную высоту



            /**
             * Попеременно меняем расположение, чтобы добиться эффекта шахматной доски
             */
            if (!alignRight) {
                alignRight = true;
                layoutDecorated(view, leftItemBorder, topItemBorder,
                        rightItemBorder / 2, bottomItemBorder);
            }
            else {
                alignRight = false;
                layoutDecorated(view, rightItemBorder / 2, topItemBorder,
                        rightItemBorder, bottomItemBorder);
            }
        }
    }

    /**
     * Добавляем возможность прокручивать LayoutManager по вертикали
     * @return true
     */
    @Override
    public boolean canScrollVertically() {
        return true;
    }

    /**
     * Прокручиваем RecyclerView по вертикали
     * @param dy дельта изменения ординат. Имеет отрицательное значение при прокрутке вверх,
     *           положительное - при прокрутке вниз
     * @param recycler
     * @param state
     * @return
     */

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (getChildCount() == 0) {
            return 0;
        }
        int scrollPath = 0; // Проскроленное расстояние. Это значение мы должны вернуть в результате
        // вызова метода. Меняется на дельту dy

        View topView = getChildAt(0); // Определяем верхний элемент
        View bottomView = getChildAt(getChildCount() - 1); // Определяем нижний элемент
        bottomItemBorder = bottomView.getBottom();

        /**
         * Если dy > 0 - мы скроллим к вниз, то есть идем к верхнему элементу. Положительное значение
         * дельты обозначает положительный инкримент ординат.
         */
        if (dy > 0) {
            int leftToTop = getDecoratedTop(topView); //leftToTop - это "осталось до верхушки"

            // Поясню тернарник: в конце концов наступает такой момент, когда мы приближаемся к верхней границе
            // верхнего элемента. И такой момент, когда дельта приращения ординат (точнее, ее абсолютное значение)
            // становится меньше того расстояния, которое требуется проскролить (наше "осталось до верхушки"). В этот момент
            // мы просто меняем дельту на это значение. И скпролим до него.
            dy = (-dy >= leftToTop) ? dy : -leftToTop;

            /**
             * Это логика добавления элемента в верхушку. Я думаю, что хорошо бы начать добавлять View из Recycle
             * заранее, чтобы добиться плавности скролла. Я добавил в leftToTop = 0 - не критично. В момент
             * нашей верхней позиции проверяем - есть ли у нас элементы, которые мы удалили в
             * Recycle сверху, и если есть - начинаем добавлять при скролле.
             */
            if (leftToTop == 0 && itemsBeginAt > 0) {

                //Сразу декриментируем счетчик, и по изменившемуся индексу достаем элеент
                itemsBeginAt--;

                //Нам нужно добавить элемент наверх, то есть нижняя грань нового элемента будет равна
                //верхней грани старой "верхушки"
                bottomItemBorder = getDecoratedTop(getChildAt(0));
                View addToFirst = recycler.getViewForPosition(itemsBeginAt);
                addView(addToFirst, 0);
                measureChildWithMargins(addToFirst, 0, 0);

                //Верхнюю грань нового элемента высчитываем
                topItemBorder = bottomItemBorder - getDecoratedMeasuredHeight(addToFirst);

                /**
                 * Это та же логика шахматной доски. Чтобы отрисовывать в едином стиле
                 */
                if ((itemsBeginAt) % 2 == 0) alignRight = false;
                else alignRight = true;

                if (!alignRight) {
                    alignRight = true;
                    layoutDecorated(addToFirst, leftItemBorder, topItemBorder,
                            rightItemBorder / 2, bottomItemBorder);
                }
                else {
                    alignRight = false;
                    layoutDecorated(addToFirst, rightItemBorder / 2, topItemBorder,
                            rightItemBorder, bottomItemBorder);
                }
            }
            /**
             * Else = dy < 0 - мы скроллим вверх, то есть идем к нижнему элементу
             */
        } else {
            int leftToBottom = getDecoratedBottom(bottomView);

            //То же самое, что и при скролле вниз. Просто отлавливаем нулевое положение
            //нижней части последнего элемента
            dy = (-dy > leftToBottom - getHeight()) ? -leftToBottom + getHeight() : dy;

            /**
             * Логика добавления в конец списка аналогична добавлению в верхушку. Последний элемент
             * мы не отслеживаем, потому что его индекс - это индекс верхушки (itemsBeginAt) +
             * количество отображаемых элементов (getChildCount - 1) + 1
             */
            if (leftToBottom - getHeight() == 0 && itemsBeginAt + getChildCount() < getItemCount()) {

                topItemBorder = getDecoratedBottom(getChildAt(getChildCount() - 1));

                View addToLast = recycler.getViewForPosition(itemsBeginAt + getChildCount());
                addView(addToLast);
                measureChildWithMargins(addToLast, 0,0);

                Log.d(TAG, "scrollVerticallyBy: pos = " + (itemsBeginAt + getChildCount()));
                bottomItemBorder = topItemBorder + getDecoratedMeasuredHeight(addToLast);

                /**
                 * Та же логика организации эффекта шахматной доски. Добавляем нечетный индекс -
                 * налево, четный индекс - направо
                 */
                if ((itemsBeginAt + getChildCount()) % 2 != 0) alignRight = false;
                else alignRight = true;

                if (!alignRight) {
                    alignRight = true;
                    layoutDecorated(addToLast, leftItemBorder, topItemBorder,
                            rightItemBorder / 2, bottomItemBorder);
                }
                else {
                    alignRight = false;
                    layoutDecorated(addToLast, rightItemBorder / 2, topItemBorder,
                            rightItemBorder, bottomItemBorder);
                }
            }
        }

        //Метод отрисовки изменений
        offsetChildrenVertical(dy);

        //Инкриментируем показатель "пройденного" пути
        scrollPath += dy;

        //Вызываем метод переиспользования View в Recycle
        recycleViews(recycler);
        return scrollPath;
    }

    /**
     * Метод переисползования View в Recycle. Я удаляю, не помещаю в Scrap-heap
     * @param recycler
     */
    private void recycleViews(RecyclerView.Recycler recycler) {

        boolean isFoundFirst = false;

        //Логика следующая: необходимо найти те View, которые нам нужно оставить, а остальные удалить
        // для переиспользования. Необходимо оставить те элементы, который хоть как-то
        // отображены на экране.

        for (int i = 0; i < getChildCount(); i++) {
            View checkView = getChildAt(i);

            // Поскольку скроллим мы только вверх и вниз, то и анализировать я буду только верхнюю и
            // нижнюю границы, левая и правая остаются неизменными
            int topOfView = getDecoratedTop(checkView);
            int bottomOfView = getDecoratedBottom(checkView);

            //isOnTheScreen - триггер элемента, который полностью отрисован на экране
            boolean isOnTheScreen = topOfView >= 0 && bottomOfView <= getHeight();

            //isNoTop - триггер элемента, верхушка которого не видна, но торчит низ
            boolean isNoTop = topOfView < 0 && bottomOfView >= 0 && bottomOfView <= getHeight();

            //isNoBottom - триггер элемента, низ которого не виден, но верхушка видна
            boolean isNoBottom = topOfView > 0 && topOfView <= getHeight() && bottomOfView >= getHeight();

            /**
             * Нам необходимо найти все такие элементы, причем пометить в этом списке первый из них и
             * последний из них. Далее, мы просто удалим из списка все элементы до первого и все после
             * последнего, а количество удаленных элементов перед первым (считаем с 1) как раз будет
             * являться индексом верхнего отображаемого элемента в оригинальном списке.
             */
            if (isOnTheScreen || isNoTop || isNoBottom)
            {
                if (!isFoundFirst) {
                    firstItemOnScreen = i;
                    isFoundFirst = true; // Первый наденный элемент
                } else lastItemOnScreen = i; // Последний найденный примет в значение последний итератор,
                //соответствующий условию (при isFoundFirst = true, разумеется)
            }
        }

        /**
         * Удаляем все элементы перед первым отрисованным View, если они есть. И инкрементируем счетчик.
         */
        for (int i = 0; i < firstItemOnScreen; i++) {
            itemsBeginAt++;
            removeAndRecycleView(getChildAt(i), recycler);
        }

        /**
         * Удаляем все элементы после последнего отрисованного View, если они есть
         */
        for (int i = lastItemOnScreen + 1; i < getChildCount(); i++) removeAndRecycleView(getChildAt(i), recycler);
    }
}

