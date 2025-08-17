<<<<<<< HEAD
package org.example.kuit_kac.global.util.dev;

import lombok.RequiredArgsConstructor;
import org.example.kuit_kac.domain.diet.repository.DietRepository;
import org.example.kuit_kac.domain.home.repository.WeightRepository;
import org.example.kuit_kac.domain.routine.repository.RoutineRepository;
import org.example.kuit_kac.domain.terms.repository.UserTermAgreementRepository;
import org.example.kuit_kac.domain.user.service.UserService;
import org.example.kuit_kac.domain.user_information.repository.UserInfoRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.example.kuit_kac.domain.user.repository.UserRepository;

@Service
@Profile("local") // local 프로필에서만 활성화
@RequiredArgsConstructor
public class DevResetService {

    private final UserService userService;
    private final UserInfoRepository userInformationRepository;
    private final UserTermAgreementRepository userTermAgreementRepository;
    private final WeightRepository weightRepository;
    private final DietRepository dietRepository;       // diet 삭제 → diet_food/diet_aifood 자동 삭제
//    private final AifoodRepository aifoodRepository;   // aifood 삭제 → diet_aifood 자동 삭제
    private final RoutineRepository routineRepository; // routine 삭제 → routine_* 자동 삭제

    public void softResetUser(long uid){
        userTermAgreementRepository.deleteAllByIdUserId(uid);
        userInformationRepository.deleteByUserId(uid);
        weightRepository.deleteAllByUserId(uid);
        dietRepository.deleteAllByUserId(uid);
        routineRepository.deleteAllByUserId(uid);
    }

    @Transactional
    public boolean deleteUserById(long userId) {
        return userService.deleteUserById(userId);
    }


}

=======
package org.example.kuit_kac.global.util.dev;public class DevResetService {
}
>>>>>>> d7022c6b7d391ae1ec2e97ba37990da81a456a4a
