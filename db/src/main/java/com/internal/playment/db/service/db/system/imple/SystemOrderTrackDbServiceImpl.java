package com.internal.playment.db.service.db.system.imple;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.internal.playment.common.table.system.SystemOrderTrackTable;
import com.internal.playment.db.mapper.system.AnewSystemOrderTrackMapper;
import com.internal.playment.db.service.db.system.SystemOrderTrackDbService;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/22
 * Time: 下午2:04
 * Description:
 */
@Service
public class SystemOrderTrackDbServiceImpl extends ServiceImpl<AnewSystemOrderTrackMapper, SystemOrderTrackTable> implements SystemOrderTrackDbService {
}
