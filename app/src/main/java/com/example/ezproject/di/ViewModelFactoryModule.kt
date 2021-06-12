package com.example.ezproject.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.Factory
import com.example.ezproject.data.repo.UnitRepo
import com.example.ezproject.data.repo.UserRepo
import com.example.ezproject.ui.activity.auth.forget.ForgetPasswordViewModel
import com.example.ezproject.ui.activity.auth.login.LoginViewModel
import com.example.ezproject.ui.activity.auth.register.RegisterViewModel
import com.example.ezproject.ui.activity.auth.splash.SplashViewModel
import com.example.ezproject.ui.fragment.chat.chatmessages.ChatRoomViewModel
import com.example.ezproject.ui.fragment.chat.chatrooms.ChatViewModel
import com.example.ezproject.ui.fragment.main.MainViewModel
import com.example.ezproject.ui.fragment.payment.PaymentsViewModel
import com.example.ezproject.ui.fragment.profile.ProfileViewModel
import com.example.ezproject.ui.fragment.profile.notification.NotificationViewModel
import com.example.ezproject.ui.fragment.profile.reviews.ReviewsViewModel
import com.example.ezproject.ui.fragment.reviews.add.AddReviewViewModel
import com.example.ezproject.ui.fragment.unit.add.AddUnitViewModel
import com.example.ezproject.ui.fragment.unit.details.UnitDetailsViewModel
import com.example.ezproject.ui.fragment.unit.favourite.FavouriteViewModel
import com.example.ezproject.ui.fragment.unit.filter.FilterViewModel
import com.example.ezproject.ui.fragment.unit.host.HostProfileViewModel
import com.example.ezproject.ui.fragment.unit.reservation.ReservationViewModel
import com.example.ezproject.ui.fragment.unit.reviews.UnitReviewsViewModel
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class ViewModelFactoryModule {
    @Provides
    @Named("splash")
    fun provideSplashViewModel(userRepo: UserRepo): Factory {
        return object : Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return SplashViewModel(userRepo) as T
            }
        }
    }

    @Provides
    @Named("login")
    fun provideLoginViewModel(userRepo: UserRepo): Factory {
        return object : Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return LoginViewModel(userRepo) as T
            }
        }
    }

    @Provides
    @Named("register")
    fun provideRegisterViewModel(userRepo: UserRepo): Factory {
        return object : Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return RegisterViewModel(userRepo) as T
            }
        }
    }

    @Provides
    @Named("forget")
    fun provideForgetViewModel(userRepo: UserRepo): Factory {
        return object : Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return ForgetPasswordViewModel(userRepo) as T
            }
        }
    }


    @Provides
    @Named("main")
    fun provideMainViewModel(userRepo: UserRepo, unitRepo: UnitRepo): Factory {
        return object : Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return MainViewModel(userRepo, unitRepo) as T
            }
        }
    }


    @Provides
    @Named("unitDetails")
    fun provideUnitDetailsViewModel(unitRepo: UnitRepo, userRepo: UserRepo): Factory {
        return object : Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return UnitDetailsViewModel(unitRepo, userRepo) as T
            }
        }
    }


    @Provides
    @Named("host")
    fun provideHostProfileViewModel(unitRepo: UnitRepo, userRepo: UserRepo): Factory {
        return object : Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return HostProfileViewModel(userRepo, unitRepo) as T
            }
        }
    }

    @Provides
    @Named("favourite")
    fun provideFavouriteViewModel(unitRepo: UnitRepo, userRepo: UserRepo): Factory {
        return object : Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return FavouriteViewModel(unitRepo, userRepo) as T
            }
        }
    }

    @Provides
    @Named("filter")
    fun provideFilterViewModel(unitRepo: UnitRepo): Factory {
        return object : Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return FilterViewModel(unitRepo) as T
            }
        }
    }

    @Provides
    @Named("reviews")
    fun provideReviewViewModel(unitRepo: UnitRepo): Factory {
        return object : Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return UnitReviewsViewModel(unitRepo) as T
            }
        }
    }

    @Provides
    @Named("reservation")
    fun provideReservatioViewModel(unitRepo: UnitRepo): Factory {
        return object : Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return ReservationViewModel(unitRepo) as T
            }
        }
    }

    @Provides
    @Named("chatrooms")
    fun provideChatRoomsViewModel(userRepo: UserRepo): Factory {
        return object : Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return ChatViewModel(userRepo) as T
            }
        }
    }


    @Provides
    @Named("chatroom")
    fun provideChatRoomViewModel(userRepo: UserRepo): Factory {
        return object : Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return ChatRoomViewModel(userRepo) as T
            }
        }
    }


    @Provides
    @Named("profile")
    fun provideProfileViewModel(userRepo: UserRepo, unitRepo: UnitRepo): Factory {
        return object : Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return ProfileViewModel(userRepo, unitRepo) as T
            }
        }
    }

    @Provides
    @Named("notification")
    fun provideNotificationViewModel(userRepo: UserRepo): Factory {
        return object : Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return NotificationViewModel(userRepo) as T
            }
        }
    }

    @Provides
    @Named("unitDraft")
    fun provideUnitDraftViewModel(unitRepo: UnitRepo, userRepo: UserRepo): Factory {
        return object : Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return AddUnitViewModel(unitRepo, userRepo) as T
            }
        }
    }


    @Provides
    @Named("addReview")
    fun provideAddReviewViewModel(unitRepo: UnitRepo, userRepo: UserRepo): Factory {
        return object : Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return AddReviewViewModel(userRepo, unitRepo) as T
            }
        }
    }

    @Provides
    @Named("payments")
    fun providePaymentsViewModel(userRepo: UserRepo): Factory {
        return object : Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return PaymentsViewModel(userRepo) as T
            }
        }
    }

    @Provides
    @Named("userReview")
    fun provideUserReviewViewModel(userRepo: UserRepo): Factory {
        return object : Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return ReviewsViewModel(userRepo) as T
            }
        }
    }

}
