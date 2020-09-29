package com.tensquare.spit.service;

import com.tensquare.spit.dao.SpitDao;
import com.tensquare.spit.pojo.Spit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import util.IdWorker;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class SpitService {
    @Autowired
    private SpitDao spitDao;
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private MongoTemplate mongoTemplate;

    public List<Spit> findAll(){
        return spitDao.findAll();
    }

    public Spit findById(String id){
        return spitDao.findById(id).get();
    }

    public void save(Spit spit){
        spit.set_id(idWorker.nextId()+"");
        spit.setPublishtime(new Date());
        spit.setVisits(0);
        spit.setShare(0);
        spit.setThumbup(0);
        spit.setComment(0);
        spit.setState("1");
        //如果当前添加的吐槽有父节点，那么父节点的吐槽回复数加1
        if(spit.getParentid() !=null && !"".equals(spit.getParentid())){

            Query query = new Query();
            query.addCriteria(Criteria.where("_id").is(spit.getParentid()));
            Update update = new Update();
            update.inc("comment",1);
            mongoTemplate.updateFirst(query, update, "spit");
        }

        spitDao.save(spit);
    }

    public void update(Spit spit){
        spitDao.save(spit);
    }

    public void deleteById(String id){
        spitDao.deleteById(id);
    }

    public Page<Spit> findByParentid(String parentid, int page, int size){
        Pageable pageable = PageRequest.of(page-1, size);
        return spitDao.findByParentid(parentid,pageable);
    }

    public void thumbup(String _id){
        /*
        //方式一:效率有问题，一次更新需要跟数据库做两次操作
        Spit spit = spitDao.findById(_id).get();
        spit.setThumbup(spit.getThumbup()==null? 1 : spit.getThumbup()+1);
        spitDao.save(spit);*/
        //方式二:使用原生的mongo命令来实现自增 update({_id,"2"},{$inc:{thumbup:1}})
        //指定条件 ====> {_id,"2"}
        Query query = new Query();
        //相当于{_id,"2"},where指定的列名也就是主键名,is传的值指得你主键值
        query.addCriteria(Criteria.where("_id").is(2));
        //需要做的更新操作 ====> {$inc:{thumbup:1}}
        Update update = new Update();
        //找到你要做的更新操作，选定key与value(你什么操作，value的字段名就叫啥 例如自增操作,inc就代表着每次自增的个数),key指你需要操作的列名
        update.inc("thumbup",1);
        mongoTemplate.updateFirst(query, update, "spit");
    }
}
