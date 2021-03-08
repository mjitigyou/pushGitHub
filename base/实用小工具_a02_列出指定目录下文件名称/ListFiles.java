public static void main(String[] args) {

        listFiles(new File("xxxxx"), "  ");
    }

    /**
     * 递归列举出指定目录下的目录
     * @param file
     * @param septor 间隔符 ==、或者空格 其实不参与的语义，辅助输出格式化
     */
    public static void listFiles(File file, String septor){
        if(file == null)return;
        if(null == septor || septor=="")septor="  ";
        String newSeptor = septor + "  ";
        if(file.exists() && file.isDirectory()){
            // 拿到名字，
            StringBuilder nameBuilder = new StringBuilder(septor + file.getName());
            nameBuilder.append("{\n");

            // 打印当前目录和子目录的名字
            File[] files = file.listFiles();
            if(files==null || files.length==0)return;
            int dirCount = 0;
            for (File temp : files) {
                // 只打印文件夹
                if(!temp.isDirectory())continue;
                dirCount++;
            }
            // 目录数目0  返回
            if(dirCount==0)return;
            for (File temp : files) {
                // 只打印文件夹
                if(!temp.isDirectory())continue;
                nameBuilder.append(newSeptor + temp.getName());
                nameBuilder.append("\n");
            }
            String endnewSeptor = newSeptor.length()>2?newSeptor.substring(0, newSeptor.length()-2):newSeptor;
            nameBuilder.append(endnewSeptor+"}\n");
            // 打印结果
            System.out.print(nameBuilder.toString());

            // 递归
            for (File file1 : files) {
                listFiles(file1, newSeptor);
            }
        }
    }
