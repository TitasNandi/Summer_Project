package elsevier;
public class Search 
{
    public int search(String text, String pattern)
    {
        String concat = pattern + "#" + text;
        int l = concat.length();
        int index=-1;
        int Z[] = new int[l];
        getZarr(concat, Z);
        for (int i = 0; i < l; ++i)
        {
            if (Z[i] == pattern.length())
                return (i - pattern.length() -1);
        }
        return index;
    }
    void getZarr(String str, int Z[])
    {
        int n = str.length();
        int L, R, k; 
        L = R = 0;
        for (int i = 1; i < n; ++i)
        {
            if (i > R)
            {
                L = R = i;
                while (R<n && str.charAt(R-L) == str.charAt(R))
                    R++;
                Z[i] = R-L;
                R--;
            }
            else
            {
                k = i-L;
                if (Z[k] < R-i+1)
                    Z[i] = Z[k];
                else
                {
                    L = i;
                    while (R<n && str.charAt(R-L) == str.charAt(R))
                        R++;
                    Z[i] = R-L;
                    R--;
                }
            }
        }
    }
}






    