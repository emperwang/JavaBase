namespace java com.wk.thrift_demo

service UserService{
    string getName(1:i32 id)
    bool isExist(1:string name)
}