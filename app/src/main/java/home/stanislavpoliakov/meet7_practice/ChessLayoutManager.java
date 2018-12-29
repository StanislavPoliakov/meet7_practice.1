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
            addView(view,i); // Добавляем View на позицию отображения
            measureChildWithMargins(view, 0, 0); //вычисляем размеры View
            bottomItemBorder = topItemBorder + getDecoratedMeasuredHeight(view); // устанавливаем нижнюю границу области в
            // новое значение, увеличенное на померянную высоту

            /**
             * Сжимаем Layout полученной View до половины экрана, чтобы содержимое корректно
             * отображалось на экране
             */
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            layoutParams.width = rightItemBorder / 2;
            view.setLayoutParams(layoutParams);

            /**
             * Если позиция четная (начиная с нулевой!), то отображаем View в левой части экрана
             * В противном случае - в правой
             */
            if (i % 2 == 0) layoutDecorated(view, leftItemBorder, topItemBorder,
                    rightItemBorder / 2, bottomItemBorder);
            else layoutDecorated(view, rightItemBorder / 2, topItemBorder,
                    rightItemBorder, bottomItemBorder);
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
    private int scrolled = 0;
    private int scrollTo = 0;

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (getChildCount() == 0) {
            return 0;
        }
        int scrolled = 0;
        View topView = getChildAt(0);
        View bottomView = getChildAt(getChildCount() - 1);
        //Log.d(TAG, "scrollVerticallyBy: dy = " + dy);
        if (dy > 0) {
            int leftToTop = getDecoratedTop(topView);
            //Log.d(TAG, "scrollVerticallyBy: left to top = " + leftToTop);
            dy = (-dy >= leftToTop) ? dy : -leftToTop;
            //offsetChildrenVertical(dy);
        } else {
            int leftToBottom = getDecoratedBottom(bottomView);
            //Log.d(TAG, "scrollVerticallyBy: left to top = " + (leftToBottom - getHeight()));
            dy = (-dy > leftToBottom - getHeight()) ? -leftToBottom + getHeight() : dy;
            //offsetChildrenVertical(dy);
        }
        offsetChildrenVertical(dy);
        scrolled += dy;
        return scrolled;
    }
}

